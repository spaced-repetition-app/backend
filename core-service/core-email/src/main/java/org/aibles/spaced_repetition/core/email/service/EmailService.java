package org.aibles.spaced_repetition.core.email.service;

import org.aibles.spaced_repetition.core.email.dto.EmailDto;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface EmailService {
    
    void sendEmail(EmailDto emailDto);
    
    CompletableFuture<Void> sendEmailAsync(EmailDto emailDto);
    
    void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables);
    
    void sendTemplateEmail(String[] to, String subject, String templateName, Map<String, Object> variables);
    
    CompletableFuture<Void> sendTemplateEmailAsync(String to, String subject, String templateName, Map<String, Object> variables);
    
    CompletableFuture<Void> sendTemplateEmailAsync(String[] to, String subject, String templateName, Map<String, Object> variables);
    
    void sendSimpleEmail(String to, String subject, String content);
    
    void sendSimpleEmail(String[] to, String subject, String content);
    
    CompletableFuture<Void> sendSimpleEmailAsync(String to, String subject, String content);
    
    CompletableFuture<Void> sendSimpleEmailAsync(String[] to, String subject, String content);
}