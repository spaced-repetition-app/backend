package org.aibles.spaced_repetition.flashcard.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aibles.spaced_repetition.shared.enums.FlashcardStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashcardFilterRequest {

    // Deck filtering
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
             message = "Invalid deck ID format")
    private String deckId;

    // Status filtering
    private FlashcardStatus status;

    // Content search
    @Size(max = 100, message = "Search query cannot exceed 100 characters")
    private String search;

    // Difficulty filtering
    @Min(value = 1, message = "Difficulty must be between 1 and 5")
    private Integer difficulty;

    // Created by filtering
    private String createdBy;

    // Pagination
    @Min(value = 0, message = "Page must be 0 or greater")
    private Integer page = 0;

    @Min(value = 1, message = "Size must be 1 or greater")
    private Integer size = 20;

    // Sorting
    private String sortBy = "createdAt";

    @Pattern(regexp = "^(asc|desc)$", message = "Sort direction must be 'asc' or 'desc'")
    private String sortDirection = "desc";

    // Include deleted flashcards (for admin)
    private Boolean includeDeleted = false;
}