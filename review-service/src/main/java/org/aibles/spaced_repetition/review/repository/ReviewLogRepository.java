package org.aibles.spaced_repetition.review.repository;

import org.aibles.spaced_repetition.review.entity.ReviewLog;
import org.aibles.spaced_repetition.shared.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLogRepository extends BaseRepository<ReviewLog> {
}