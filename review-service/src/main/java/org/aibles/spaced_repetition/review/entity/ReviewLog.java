package org.aibles.spaced_repetition.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aibles.spaced_repetition.shared.entity.BaseEntity;
import org.aibles.spaced_repetition.shared.enums.ReviewResult;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewLog extends BaseEntity {
    
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;
    
    @Column(name = "flashcard_id", nullable = false, length = 36)
    private String flashcardId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewResult result;
    
    @Column(name = "reaction_time_ms")
    private Integer reactionTimeMs;
    
    @Column(name = "silent_mode", nullable = false)
    private Boolean silentMode = false;
    
    @Column(name = "next_review_date")
    private LocalDateTime nextReviewDate;
}