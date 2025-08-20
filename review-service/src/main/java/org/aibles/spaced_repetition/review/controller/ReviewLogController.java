package org.aibles.spaced_repetition.review.controller;

import org.aibles.spaced_repetition.review.entity.ReviewLog;
import org.aibles.spaced_repetition.review.service.ReviewLogService;
import org.aibles.spaced_repetition.shared.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review-logs")
public class ReviewLogController extends BaseController<ReviewLog> {
    
    public ReviewLogController(ReviewLogService service) {
        super(service);
    }
    
    @Override
    protected String getEntityName() {
        return "ReviewLog";
    }
}