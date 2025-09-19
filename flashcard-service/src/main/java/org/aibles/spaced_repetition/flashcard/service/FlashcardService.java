package org.aibles.spaced_repetition.flashcard.service;

import lombok.extern.slf4j.Slf4j;
import org.aibles.spaced_repetition.flashcard.dto.CreateFlashcardRequest;
import org.aibles.spaced_repetition.flashcard.dto.UpdateFlashcardRequest;
import org.aibles.spaced_repetition.flashcard.dto.FlashcardDto;
import org.aibles.spaced_repetition.flashcard.dto.FlashcardFilterRequest;
import org.aibles.spaced_repetition.flashcard.entity.Deck;
import org.aibles.spaced_repetition.flashcard.entity.Flashcard;
import org.aibles.spaced_repetition.flashcard.exception.FlashcardBusinessException;
import org.aibles.spaced_repetition.shared.exception.ResourceNotFoundException;
import org.aibles.spaced_repetition.flashcard.repository.FlashcardRepository;
import org.aibles.spaced_repetition.shared.enums.FlashcardStatus;
import org.aibles.spaced_repetition.shared.service.BaseServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@Transactional
public class FlashcardService extends BaseServiceImpl<Flashcard> {

    private static final int MAX_FLASHCARDS_PER_DECK = 1000;

    private final FlashcardRepository flashcardRepository;
    private final DeckService deckService;
    private final ApplicationEventPublisher eventPublisher;

    public FlashcardService(FlashcardRepository repository,
                           DeckService deckService,
                           ApplicationEventPublisher eventPublisher) {
        super(repository);
        this.flashcardRepository = repository;
        this.deckService = deckService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    protected String getEntityName() {
        return "Flashcard";
    }

    @Transactional
    public FlashcardDto createFlashcard(CreateFlashcardRequest request) {
        log.info("Creating flashcard for deck: {}", request.getDeckId());

        // Business validation
        validateFlashcardCreation(request);

        // Create and save flashcard
        Flashcard flashcard = buildFlashcardFromRequest(request);
        Flashcard savedFlashcard = create(flashcard);

        // Publish domain event
        // eventPublisher.publishEvent(new FlashcardCreatedEvent(savedFlashcard));

        log.info("Successfully created flashcard with ID: {}", savedFlashcard.getId());
        return mapToDto(savedFlashcard);
    }

    /**
     * Get all flashcards with filtering and pagination
     *
     * @param filterRequest The filter and pagination parameters
     * @return Paginated list of flashcards
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "flashcards-list-cache", key = "#filterRequest.hashCode()", unless = "#result.isEmpty()")
    public Page<FlashcardDto> getAllFlashcards(FlashcardFilterRequest filterRequest) {
        log.info("Fetching flashcards with filters: deckId={}, status={}, search={}, page={}, size={}",
                filterRequest.getDeckId(), filterRequest.getStatus(), filterRequest.getSearch(),
                filterRequest.getPage(), filterRequest.getSize());

        // Validate pagination parameters
        validatePaginationParameters(filterRequest);

        // Build sorting
        Sort sort = buildSort(filterRequest.getSortBy(), filterRequest.getSortDirection());

        // Create pageable
        Pageable pageable = PageRequest.of(
                filterRequest.getPage(),
                Math.min(filterRequest.getSize(), 100), // Max 100 items per page
                sort
        );

        // Build specification for filtering
        Specification<Flashcard> spec = buildFlashcardSpecification(filterRequest);

        // Execute query with specifications
        Page<Flashcard> flashcardPage = flashcardRepository.findAll(spec, pageable);

        // Convert to DTOs
        Page<FlashcardDto> result = flashcardPage.map(this::mapToDto);

        log.info("Successfully retrieved {} flashcards (page {} of {})",
                result.getNumberOfElements(), result.getNumber() + 1, result.getTotalPages());

        return result;
    }

    /**
     * Get flashcard by ID with business validation
     *
     * @param id The flashcard ID
     * @return FlashcardDto
     * @throws ResourceNotFoundException if flashcard not found
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "flashcard-cache", key = "#id", unless = "#result == null")
    public FlashcardDto getFlashcardById(String id) {
        log.debug("Fetching flashcard with ID: {}", id);

        // Validate ID format
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Flashcard ID cannot be null or empty");
        }

        // Validate UUID format
        if (!isValidUUID(id)) {
            log.warn("Invalid UUID format for flashcard ID: {}", id);
            throw new FlashcardBusinessException("INVALID_ID_FORMAT",
                    "Invalid flashcard ID format. Must be a valid UUID");
        }

        // Find flashcard with caching potential
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Flashcard not found with ID: {}", id);
                    return new ResourceNotFoundException("Flashcard", id);
                });

        // Check if flashcard is accessible (not deleted, etc.)
        validateFlashcardAccess(flashcard);

        // Track view metrics
        // metricsService.trackFlashcardView(id);

        log.debug("Successfully retrieved flashcard with ID: {}", id);
        return mapToDto(flashcard);
    }

    /**
     * Update flashcard with comprehensive validation
     *
     * @param id The flashcard ID
     * @param request Update request
     * @return Updated flashcard DTO
     */
    @Transactional
    public FlashcardDto updateFlashcard(String id, UpdateFlashcardRequest request) {
        log.info("Updating flashcard with ID: {}", id);

        // Validate ID
        if (!isValidUUID(id)) {
            throw new FlashcardBusinessException("INVALID_ID_FORMAT",
                    "Invalid flashcard ID format. Must be a valid UUID");
        }

        // Find existing flashcard
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard", id));

        // Validate update permissions
        validateUpdateAccess(flashcard);

        // Check for duplicate content if content is being changed
        if (!flashcard.getFront().equals(request.getFront().trim()) ||
            !flashcard.getBack().equals(request.getBack().trim())) {
            validateNoDuplicateContent(flashcard.getDeckId(), request.getFront(), request.getBack(), id);
        }

        // Store old values for audit
        String oldFront = flashcard.getFront();
        String oldBack = flashcard.getBack();
        FlashcardStatus oldStatus = flashcard.getStatus();

        // Update fields
        flashcard.setFront(request.getFront().trim());
        flashcard.setBack(request.getBack().trim());

        // Update status if provided
        if (request.getStatus() != null) {
            validateStatusTransition(flashcard.getStatus(), request.getStatus());
            flashcard.setStatus(request.getStatus());
        }

        // Save updated flashcard
        Flashcard updatedFlashcard = flashcardRepository.save(flashcard);

        // Log audit information
        logUpdate(id, oldFront, oldBack, oldStatus, updatedFlashcard);

        // Clear cache for this flashcard
        // cacheManager.evictFlashcard(id);

        log.info("Successfully updated flashcard with ID: {}", id);
        return mapToDto(updatedFlashcard);
    }

    private void validateUpdateAccess(Flashcard flashcard) {
        // Check if flashcard can be updated
        if (flashcard.getStatus() == FlashcardStatus.DELETED) {
            throw new FlashcardBusinessException("FLASHCARD_DELETED",
                    "Cannot update a deleted flashcard");
        }

        // Additional permission checks can be added here
        // e.g., check if current user is the owner
    }

    private void validateStatusTransition(FlashcardStatus currentStatus, FlashcardStatus newStatus) {
        // Validate allowed status transitions
        if (currentStatus == FlashcardStatus.DELETED && newStatus != FlashcardStatus.DELETED) {
            throw new FlashcardBusinessException("INVALID_STATUS_TRANSITION",
                    "Cannot restore a deleted flashcard");
        }

        if (currentStatus == FlashcardStatus.MASTERED && newStatus == FlashcardStatus.NEW) {
            log.warn("Resetting mastered flashcard to NEW status");
        }
    }

    private void validateNoDuplicateContent(String deckId, String front, String back, String excludeId) {
        boolean duplicateExists = flashcardRepository.findByDeckIdAndFrontAndBack(
                deckId, front.trim(), back.trim())
                .stream()
                .anyMatch(f -> !f.getId().equals(excludeId));

        if (duplicateExists) {
            throw new FlashcardBusinessException("DUPLICATE_FLASHCARD",
                    "Another flashcard with the same content already exists in this deck");
        }
    }

    private void logUpdate(String id, String oldFront, String oldBack,
                          FlashcardStatus oldStatus, Flashcard updated) {
        log.info("Flashcard {} updated - Front changed: {}, Back changed: {}, Status changed: {}",
                id,
                !oldFront.equals(updated.getFront()),
                !oldBack.equals(updated.getBack()),
                oldStatus != updated.getStatus());
    }

    /**
     * Soft delete flashcard - marks as DELETED status
     *
     * @param id The flashcard ID
     */
    @Transactional
    public void softDeleteFlashcard(String id) {
        log.info("Soft deleting flashcard with ID: {}", id);

        // Validate ID
        if (!isValidUUID(id)) {
            throw new FlashcardBusinessException("INVALID_ID_FORMAT",
                    "Invalid flashcard ID format. Must be a valid UUID");
        }

        // Find existing flashcard
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard", id));

        // Validate delete permissions
        validateDeleteAccess(flashcard);

        // Check if already deleted
        if (flashcard.getStatus() == FlashcardStatus.DELETED) {
            log.warn("Flashcard {} is already deleted", id);
            throw new FlashcardBusinessException("ALREADY_DELETED",
                    "Flashcard is already deleted");
        }

        // Store original status for audit
        FlashcardStatus originalStatus = flashcard.getStatus();

        // Mark as deleted
        flashcard.setStatus(FlashcardStatus.DELETED);
        flashcardRepository.save(flashcard);

        // Log deletion
        log.info("Flashcard {} soft deleted. Original status was: {}", id, originalStatus);

        // Clear cache
        // cacheManager.evictFlashcard(id);

        // Publish deletion event
        // eventPublisher.publishEvent(new FlashcardDeletedEvent(id, false));
    }

    /**
     * Permanently delete flashcard from database
     *
     * @param id The flashcard ID
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void permanentlyDeleteFlashcard(String id) {
        log.warn("Permanently deleting flashcard with ID: {}", id);

        // Validate ID
        if (!isValidUUID(id)) {
            throw new FlashcardBusinessException("INVALID_ID_FORMAT",
                    "Invalid flashcard ID format. Must be a valid UUID");
        }

        // Find existing flashcard
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard", id));

        // Validate admin permissions (handled by @PreAuthorize)

        // Check for related data
        checkRelatedDataBeforeDelete(id);

        // Store info for audit before deletion
        String deckId = flashcard.getDeckId();
        String front = flashcard.getFront();
        FlashcardStatus status = flashcard.getStatus();

        // Permanently delete
        flashcardRepository.deleteById(id);

        // Log permanent deletion
        log.warn("Flashcard {} permanently deleted from deck {}. Front: '{}', Status: {}",
                id, deckId, front, status);

        // Clear cache
        // cacheManager.evictFlashcard(id);

        // Publish permanent deletion event
        // eventPublisher.publishEvent(new FlashcardDeletedEvent(id, true));
    }

    private void validateDeleteAccess(Flashcard flashcard) {
        // Additional permission checks can be added here
        // e.g., check if current user is the owner or has permission

        // Check if flashcard is part of active review session
        // if (reviewService.hasActiveReviews(flashcard.getId())) {
        //     throw new FlashcardBusinessException("ACTIVE_REVIEWS",
        //             "Cannot delete flashcard with active review sessions");
        // }
    }

    private void checkRelatedDataBeforeDelete(String flashcardId) {
        // Check for related review logs
        // long reviewCount = reviewLogRepository.countByFlashcardId(flashcardId);
        // if (reviewCount > 0) {
        //     log.warn("Deleting flashcard {} will cascade delete {} review logs",
        //             flashcardId, reviewCount);
        // }

        // Check for related progress data
        // if (progressRepository.existsByFlashcardId(flashcardId)) {
        //     log.warn("Deleting flashcard {} will affect user progress tracking", flashcardId);
        // }
    }

    private void validateFlashcardAccess(Flashcard flashcard) {
        // Check if flashcard is in accessible state
        if (flashcard.getStatus() == FlashcardStatus.DELETED) {
            throw new FlashcardBusinessException("FLASHCARD_DELETED",
                    "Flashcard has been deleted and is no longer accessible");
        }
    }

    private void validateFlashcardCreation(CreateFlashcardRequest request) {
        // Check deck exists and is accessible
        Deck deck = deckService.findById(request.getDeckId())
                .orElseThrow(() -> new ResourceNotFoundException("Deck", request.getDeckId()));

        // Check deck capacity
        long currentCount = flashcardRepository.countByDeckId(request.getDeckId());
        if (currentCount >= MAX_FLASHCARDS_PER_DECK) {
            throw FlashcardBusinessException.deckCapacityExceeded(request.getDeckId(), MAX_FLASHCARDS_PER_DECK);
        }

        // Check for duplicate content in the same deck
        boolean duplicateExists = flashcardRepository.existsByDeckIdAndFrontAndBack(
                request.getDeckId(),
                request.getFront().trim(),
                request.getBack().trim()
        );

        if (duplicateExists) {
            throw FlashcardBusinessException.duplicateFlashcard(request.getFront(), request.getBack());
        }
    }

    private Flashcard buildFlashcardFromRequest(CreateFlashcardRequest request) {
        Flashcard flashcard = new Flashcard();
        flashcard.setFront(request.getFront().trim());
        flashcard.setBack(request.getBack().trim());
        flashcard.setDeckId(request.getDeckId());
        flashcard.setStatus(FlashcardStatus.NEW);

        return flashcard;
    }

    private FlashcardDto mapToDto(Flashcard flashcard) {
        FlashcardDto dto = new FlashcardDto();
        dto.setId(flashcard.getId());
        dto.setFront(flashcard.getFront());
        dto.setBack(flashcard.getBack());
        dto.setDeckId(flashcard.getDeckId());
        dto.setStatus(flashcard.getStatus());
        dto.setCreatedBy(flashcard.getCreatedBy());
        dto.setCreatedAt(flashcard.getCreatedAt());
        dto.setLastModifiedBy(flashcard.getUpdatedBy());
        dto.setLastModifiedAt(flashcard.getUpdatedAt());
        return dto;
    }

    private void validatePaginationParameters(FlashcardFilterRequest filterRequest) {
        if (filterRequest.getPage() < 0) {
            throw new FlashcardBusinessException("INVALID_PAGINATION",
                    "Page number cannot be negative");
        }

        if (filterRequest.getSize() <= 0) {
            throw new FlashcardBusinessException("INVALID_PAGINATION",
                    "Page size must be greater than 0");
        }

        if (filterRequest.getSize() > 100) {
            log.warn("Requested page size {} exceeds maximum allowed (100), limiting to 100",
                    filterRequest.getSize());
        }
    }

    private Sort buildSort(String sortBy, String sortDirection) {
        // Validate sort field
        String[] allowedFields = {"createdAt", "updatedAt", "front", "back", "status"};
        boolean isValidField = false;
        for (String field : allowedFields) {
            if (field.equals(sortBy)) {
                isValidField = true;
                break;
            }
        }

        if (!isValidField) {
            log.warn("Invalid sort field '{}', defaulting to 'createdAt'", sortBy);
            sortBy = "createdAt";
        }

        // Build sort direction
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return Sort.by(direction, sortBy);
    }

    private Specification<Flashcard> buildFlashcardSpecification(FlashcardFilterRequest filterRequest) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            // Filter by deck ID
            if (StringUtils.hasText(filterRequest.getDeckId())) {
                if (isValidUUID(filterRequest.getDeckId())) {
                    predicates.add(criteriaBuilder.equal(root.get("deckId"), filterRequest.getDeckId()));
                } else {
                    log.warn("Invalid deck ID format in filter: {}", filterRequest.getDeckId());
                    throw new FlashcardBusinessException("INVALID_DECK_ID_FORMAT",
                            "Invalid deck ID format in filter");
                }
            }

            // Filter by status
            if (filterRequest.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filterRequest.getStatus()));
            } else if (!Boolean.TRUE.equals(filterRequest.getIncludeDeleted())) {
                // By default, exclude deleted flashcards unless explicitly requested
                predicates.add(criteriaBuilder.notEqual(root.get("status"), FlashcardStatus.DELETED));
            }

            // Filter by search term (searches in front and back content)
            if (StringUtils.hasText(filterRequest.getSearch())) {
                String searchPattern = "%" + filterRequest.getSearch().toLowerCase().trim() + "%";
                var frontPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("front")), searchPattern);
                var backPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("back")), searchPattern);
                predicates.add(criteriaBuilder.or(frontPredicate, backPredicate));
            }

            // Filter by created by (if provided)
            if (StringUtils.hasText(filterRequest.getCreatedBy())) {
                predicates.add(criteriaBuilder.equal(root.get("createdBy"), filterRequest.getCreatedBy()));
            }

            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    private boolean isValidUUID(String uuid) {
        if (uuid == null) return false;
        try {
            // Check if it matches UUID pattern
            return uuid.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        } catch (Exception e) {
            return false;
        }
    }
}