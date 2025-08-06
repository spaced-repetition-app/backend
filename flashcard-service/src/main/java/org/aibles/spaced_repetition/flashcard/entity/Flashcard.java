package org.aibles.spaced_repetition.flashcard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aibles.spaced_repetition.shared.entity.BaseEntity;
import org.aibles.spaced_repetition.shared.enums.FlashcardStatus;

@Entity
@Table(name = "flashcards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flashcard extends BaseEntity {
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String front;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String back;
    
    @Column(name = "deck_id", nullable = false, length = 36)
    private String deckId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlashcardStatus status = FlashcardStatus.NEW;
}