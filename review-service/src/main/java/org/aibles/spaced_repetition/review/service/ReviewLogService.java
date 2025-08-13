package org.aibles.spaced_repetition.review.service;

import org.aibles.spaced_repetition.review.entity.ReviewLog;
import org.aibles.spaced_repetition.review.repository.ReviewLogRepository;
import org.aibles.spaced_repetition.shared.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ReviewLogService extends BaseServiceImpl<ReviewLog> {
    
    public ReviewLogService(ReviewLogRepository repository) {
        super(repository);
    }
    
    @Override
    protected String getEntityName() {
        return "ReviewLog";
    }
}