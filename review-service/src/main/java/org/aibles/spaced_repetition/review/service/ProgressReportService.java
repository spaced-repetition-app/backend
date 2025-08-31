package org.aibles.spaced_repetition.review.service;

import org.aibles.spaced_repetition.review.entity.ProgressReport;
import org.aibles.spaced_repetition.review.repository.ProgressReportRepository;
import org.aibles.spaced_repetition.shared.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ProgressReportService extends BaseServiceImpl<ProgressReport> {
    
    public ProgressReportService(ProgressReportRepository repository) {
        super(repository);
    }
    
    @Override
    protected String getEntityName() {
        return "ProgressReport";
    }
}