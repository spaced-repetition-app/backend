package org.aibles.spaced_repetition.flashcard.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aibles.spaced_repetition.flashcard.dto.CreateFlashcardRequest;
import org.aibles.spaced_repetition.flashcard.dto.UpdateFlashcardRequest;
import org.aibles.spaced_repetition.flashcard.dto.FlashcardDto;
import org.aibles.spaced_repetition.flashcard.dto.FlashcardFilterRequest;
import org.aibles.spaced_repetition.flashcard.service.FlashcardService;
import org.aibles.spaced_repetition.shared.dto.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/flashcards")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, allowCredentials = "true")
public class FlashcardController {

    private final FlashcardService flashcardService;

    /**
     * Get all flashcards with filtering and pagination
     *
     * @param filterRequest The filter and pagination parameters
     * @return Paginated list of flashcards
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<Page<FlashcardDto>>> getAllFlashcards(
            @Valid @ModelAttribute FlashcardFilterRequest filterRequest) {

        log.info("Received request to get all flashcards with filters: deckId={}, status={}, search={}, page={}, size={}",
                filterRequest.getDeckId(), filterRequest.getStatus(), filterRequest.getSearch(),
                filterRequest.getPage(), filterRequest.getSize());

        Page<FlashcardDto> flashcards = flashcardService.getAllFlashcards(filterRequest);

        log.info("Successfully retrieved {} flashcards (page {} of {})",
                flashcards.getNumberOfElements(), flashcards.getNumber() + 1, flashcards.getTotalPages());

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(flashcards.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(flashcards.getTotalPages()))
                .header("Cache-Control", "max-age=30")
                .body(BaseResponse.success("Flashcards retrieved successfully", flashcards));
    }

    /**
     * Create a new flashcard
     *
     * @param request The flashcard creation request
     * @return Created flashcard with metadata
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<FlashcardDto>> createFlashcard(
            @Valid @RequestBody CreateFlashcardRequest request) {

        log.info("Received request to create flashcard for deck: {}", request.getDeckId());

        FlashcardDto createdFlashcard = flashcardService.createFlashcard(request);

        log.info("Successfully created flashcard with ID: {} for deck: {}",
                createdFlashcard.getId(), request.getDeckId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("X-Resource-ID", createdFlashcard.getId())
                .body(BaseResponse.success("Flashcard created successfully", createdFlashcard));
    }

    /**
     * Get flashcard by ID
     *
     * @param id The flashcard ID
     * @return Flashcard details
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<FlashcardDto>> getFlashcardById(
            @PathVariable String id) {

        log.info("Received request to get flashcard with ID: {}", id);

        FlashcardDto flashcard = flashcardService.getFlashcardById(id);

        log.info("Successfully retrieved flashcard with ID: {}", id);

        return ResponseEntity.ok()
                .header("Cache-Control", "max-age=60")
                .body(BaseResponse.success(flashcard));
    }

    /**
     * Update flashcard by ID
     *
     * @param id The flashcard ID
     * @param request The update request
     * @return Updated flashcard
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<FlashcardDto>> updateFlashcard(
            @PathVariable String id,
            @Valid @RequestBody UpdateFlashcardRequest request) {

        log.info("Received request to update flashcard with ID: {}", id);

        FlashcardDto updatedFlashcard = flashcardService.updateFlashcard(id, request);

        log.info("Successfully updated flashcard with ID: {}", id);

        return ResponseEntity.ok()
                .header("X-Resource-ID", updatedFlashcard.getId())
                .header("Cache-Control", "no-cache")
                .body(BaseResponse.success("Flashcard updated successfully", updatedFlashcard));
    }

    /**
     * Delete flashcard by ID (soft delete)
     *
     * @param id The flashcard ID
     * @param permanent Optional parameter for permanent deletion (admin only)
     * @return Deletion confirmation
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<Void>> deleteFlashcard(
            @PathVariable String id,
            @RequestParam(value = "permanent", defaultValue = "false") boolean permanent) {

        log.info("Received request to delete flashcard with ID: {} (permanent: {})", id, permanent);

        if (permanent) {
            // Only admins can permanently delete
            flashcardService.permanentlyDeleteFlashcard(id);
            log.info("Permanently deleted flashcard with ID: {}", id);
            return ResponseEntity.ok()
                    .body(BaseResponse.success("Flashcard permanently deleted", null));
        } else {
            // Soft delete - mark as DELETED
            flashcardService.softDeleteFlashcard(id);
            log.info("Soft deleted flashcard with ID: {}", id);
            return ResponseEntity.ok()
                    .body(BaseResponse.success("Flashcard deleted successfully", null));
        }
    }
}