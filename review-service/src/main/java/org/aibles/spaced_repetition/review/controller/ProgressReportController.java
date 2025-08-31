package org.aibles.spaced_repetition.review.controller;

import org.aibles.spaced_repetition.review.entity.ProgressReport;
import org.aibles.spaced_repetition.review.service.ProgressReportService;
import org.aibles.spaced_repetition.shared.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress-reports")
public class ProgressReportController extends BaseController<ProgressReport> {
    
    public ProgressReportController(ProgressReportService service) {
        super(service);
    }
    
    @Override
    protected String getEntityName() {
        return "ProgressReport";
    }
}