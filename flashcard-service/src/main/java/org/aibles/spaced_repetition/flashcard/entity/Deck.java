package org.aibles.spaced_repetition.flashcard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aibles.spaced_repetition.shared.entity.BaseEntity;

@Entity
@Table(name = "decks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deck extends BaseEntity {
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "owner_id", nullable = false, length = 36)
    private String ownerId;
}