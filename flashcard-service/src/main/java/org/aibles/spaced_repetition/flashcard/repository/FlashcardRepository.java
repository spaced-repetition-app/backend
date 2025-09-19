package org.aibles.spaced_repetition.flashcard.repository;

import org.aibles.spaced_repetition.flashcard.entity.Flashcard;
import org.aibles.spaced_repetition.shared.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardRepository extends BaseRepository<Flashcard>, JpaSpecificationExecutor<Flashcard> {

    /**
     * Count flashcards in a specific deck
     */
    long countByDeckId(String deckId);

    /**
     * Check if flashcard with same content exists in deck
     */
    boolean existsByDeckIdAndFrontAndBack(String deckId, String front, String back);

    /**
     * Find flashcards by deck ID and content
     */
    List<Flashcard> findByDeckIdAndFrontAndBack(String deckId, String front, String back);

    /**
     * Find flashcards by deck ID with pagination support
     */
    List<Flashcard> findByDeckIdOrderByCreatedAtDesc(String deckId);

    /**
     * Check if deck has any flashcards
     */
    boolean existsByDeckId(String deckId);

    /**
     * Find duplicates in deck (for data cleanup)
     */
    @Query("""
        SELECT f FROM Flashcard f WHERE f.deckId = :deckId
        AND EXISTS (
            SELECT f2 FROM Flashcard f2
            WHERE f2.deckId = :deckId
            AND f2.front = f.front
            AND f2.back = f.back
            AND f2.id != f.id
        )
        """)
    List<Flashcard> findDuplicatesInDeck(@Param("deckId") String deckId);
}