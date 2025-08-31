package org.aibles.spaced_repetition.core.email.dto;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
public class AttachmentDto {
    
    @NotBlank(message = "Filename cannot be blank")
    private String filename;
    
    @NotBlank(message = "Content type cannot be blank")
    private String contentType;
    
    private byte[] content;
    private String filePath;
    
    @Builder.Default
    private boolean inline = false;
    
    private String contentId;
}