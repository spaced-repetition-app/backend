package org.aibles.spaced_repetition.review.repository;

import org.aibles.spaced_repetition.review.entity.ProgressReport;
import org.aibles.spaced_repetition.shared.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressReportRepository extends BaseRepository<ProgressReport> {
}