package org.aibles.spaced_repetition.flashcard.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aibles.spaced_repetition.shared.enums.FlashcardStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFlashcardRequest {

    @NotBlank(message = "Front content is required")
    @Size(min = 1, max = 2000, message = "Front content must be between 1 and 2000 characters")
    @Pattern(regexp = "^(?!\\s*$).+", message = "Front content cannot be only whitespace")
    private String front;

    @NotBlank(message = "Back content is required")
    @Size(min = 1, max = 2000, message = "Back content must be between 1 and 2000 characters")
    @Pattern(regexp = "^(?!\\s*$).+", message = "Back content cannot be only whitespace")
    private String back;

    // Allow status update (for marking as SUSPENDED, MASTERED, etc.)
    private FlashcardStatus status;

    // Optional fields
    @Size(max = 500, message = "Hint cannot exceed 500 characters")
    private String hint;

    @Size(max = 1000, message = "Explanation cannot exceed 1000 characters")
    private String explanation;

    @Min(value = 1, message = "Difficulty must be between 1 and 5")
    @Max(value = 5, message = "Difficulty must be between 1 and 5")
    private Integer difficulty;
}