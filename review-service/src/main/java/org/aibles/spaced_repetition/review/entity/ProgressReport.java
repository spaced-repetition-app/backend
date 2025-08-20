package org.aibles.spaced_repetition.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aibles.spaced_repetition.shared.entity.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "progress_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressReport extends BaseEntity {
    
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;
    
    @Column(name = "total_new", nullable = false)
    private Integer totalNew = 0;
    
    @Column(name = "total_learning", nullable = false)
    private Integer totalLearning = 0;
    
    @Column(name = "total_review", nullable = false)
    private Integer totalReview = 0;
    
    @Column(name = "total_mastered", nullable = false)
    private Integer totalMastered = 0;
    
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal accuracy = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private Integer streak = 0;
}