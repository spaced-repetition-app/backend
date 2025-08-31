package org.aibles.spaced_repetition.core.email.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aibles.spaced_repetition.core.email.dto.AttachmentDto;
import org.aibles.spaced_repetition.core.email.dto.EmailDto;
import org.aibles.spaced_repetition.core.email.service.EmailService;
import org.aibles.spaced_repetition.core.exception.CoreInternalServerException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendEmail(EmailDto emailDto) {
        try {
            if (emailDto.isHtml() || emailDto.getAttachments() != null || StringUtils.hasText(emailDto.getTemplateName())) {
                sendMimeMessage(emailDto);
            } else {
                sendSimpleMessage(emailDto);
            }
            log.info("Email sent successfully to: {}", emailDto.getTo());
        } catch (Exception e) {
            log.error("Failed to send email to: {}", emailDto.getTo(), e);
            throw new CoreInternalServerException("email.sent.failed");
        }
    }

    @Override
    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendEmailAsync(EmailDto emailDto) {
        return CompletableFuture.runAsync(() -> sendEmail(emailDto));
    }

    @Override
    public void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        sendTemplateEmail(new String[]{to}, subject, templateName, variables);
    }

    @Override
    public void sendTemplateEmail(String[] to, String subject, String templateName, Map<String, Object> variables) {
        EmailDto emailDto = EmailDto.builder()
                .to(Arrays.asList(to))
                .subject(subject)
                .templateName(templateName)
                .templateVariables(variables)
                .isHtml(true)
                .build();
        sendEmail(emailDto);
    }

    @Override
    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendTemplateEmailAsync(String to, String subject, String templateName, Map<String, Object> variables) {
        return CompletableFuture.runAsync(() -> sendTemplateEmail(to, subject, templateName, variables));
    }

    @Override
    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendTemplateEmailAsync(String[] to, String subject, String templateName, Map<String, Object> variables) {
        return CompletableFuture.runAsync(() -> sendTemplateEmail(to, subject, templateName, variables));
    }

    @Override
    public void sendSimpleEmail(String to, String subject, String content) {
        sendSimpleEmail(new String[]{to}, subject, content);
    }

    @Override
    public void sendSimpleEmail(String[] to, String subject, String content) {
        EmailDto emailDto = EmailDto.builder()
                .to(Arrays.asList(to))
                .subject(subject)
                .textContent(content)
                .isHtml(false)
                .build();
        sendEmail(emailDto);
    }

    @Override
    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendSimpleEmailAsync(String to, String subject, String content) {
        return CompletableFuture.runAsync(() -> sendSimpleEmail(to, subject, content));
    }

    @Override
    @Async("emailTaskExecutor")
    public CompletableFuture<Void> sendSimpleEmailAsync(String[] to, String subject, String content) {
        return CompletableFuture.runAsync(() -> sendSimpleEmail(to, subject, content));
    }

    private void sendSimpleMessage(EmailDto emailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDto.getTo().toArray(new String[0]));
        if (emailDto.getCc() != null && !emailDto.getCc().isEmpty()) {
            message.setCc(emailDto.getCc().toArray(new String[0]));
        }
        if (emailDto.getBcc() != null && !emailDto.getBcc().isEmpty()) {
            message.setBcc(emailDto.getBcc().toArray(new String[0]));
        }
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getTextContent());
        
        mailSender.send(message);
    }

    private void sendMimeMessage(EmailDto emailDto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(emailDto.getTo().toArray(new String[0]));
        if (emailDto.getCc() != null && !emailDto.getCc().isEmpty()) {
            helper.setCc(emailDto.getCc().toArray(new String[0]));
        }
        if (emailDto.getBcc() != null && !emailDto.getBcc().isEmpty()) {
            helper.setBcc(emailDto.getBcc().toArray(new String[0]));
        }
        helper.setSubject(emailDto.getSubject());

        String content = getEmailContent(emailDto);
        helper.setText(content, emailDto.isHtml());

        if (emailDto.getAttachments() != null) {
            addAttachments(helper, emailDto.getAttachments());
        }

        mailSender.send(message);
    }

    private String getEmailContent(EmailDto emailDto) {
        if (StringUtils.hasText(emailDto.getTemplateName())) {
            return processTemplate(emailDto.getTemplateName(), emailDto.getTemplateVariables());
        } else if (emailDto.isHtml() && StringUtils.hasText(emailDto.getHtmlContent())) {
            return emailDto.getHtmlContent();
        } else {
            return emailDto.getTextContent();
        }
    }

    private String processTemplate(String templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            if (variables != null) {
                context.setVariables(variables);
            }
            return templateEngine.process("email/" + templateName, context);
        } catch (Exception e) {
            log.error("Failed to process email template: {}", templateName, e);
            throw new CoreInternalServerException("email.template.not.found");
        }
    }

    private void addAttachments(MimeMessageHelper helper, List<AttachmentDto> attachments) throws MessagingException {
        for (AttachmentDto attachment : attachments) {
            if (attachment.getContent() != null) {
                ByteArrayResource resource = new ByteArrayResource(attachment.getContent());
                if (attachment.isInline() && StringUtils.hasText(attachment.getContentId())) {
                    helper.addInline(attachment.getContentId(), resource, attachment.getContentType());
                } else {
                    helper.addAttachment(attachment.getFilename(), resource);
                }
            } else if (StringUtils.hasText(attachment.getFilePath())) {
                File file = new File(attachment.getFilePath());
                if (file.exists()) {
                    FileSystemResource resource = new FileSystemResource(file);
                    if (attachment.isInline() && StringUtils.hasText(attachment.getContentId())) {
                        helper.addInline(attachment.getContentId(), resource);
                    } else {
                        helper.addAttachment(attachment.getFilename(), resource);
                    }
                }
            }
        }
    }
}