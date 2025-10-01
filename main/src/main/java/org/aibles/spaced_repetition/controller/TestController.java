package org.aibles.spaced_repetition.controller;

import org.aibles.spaced_repetition.service.TestSentryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestSentryService testSentryService;

    @GetMapping("/error")
    public String testError() {
        throw new RuntimeException("Test exception for Sentry integration");
    }
    
    @GetMapping("/illegal-argument")
    public String testIllegalArgument() {
        throw new IllegalArgumentException("Test illegal argument exception");
    }

    @GetMapping("/sentry/service")
    public String testSentryService() {
        testSentryService.testExceptionCapture();
        return "Exception sent to Sentry via service";
    }

    @GetMapping("/sentry/user-context")
    public String testSentryWithUserContext() {
        testSentryService.testWithUserContext();
        return "Exception with user context sent to Sentry";
    }

    @GetMapping("/sentry/breadcrumbs")
    public String testSentryWithBreadcrumbs() {
        testSentryService.testCustomBreadcrumbs();
        return "Exception with breadcrumbs sent to Sentry";
    }

    @GetMapping("/logs/info")
    public String testInfoLog(@RequestParam(defaultValue = "Test info message") String message) {
        MDC.put("userId", "user123");
        MDC.put("action", "test-log");
        logger.info("Info log test: {}", message);
        MDC.clear();
        return "Info log sent successfully";
    }

    @GetMapping("/logs/warn")
    public String testWarnLog(@RequestParam(defaultValue = "Test warning message") String message) {
        MDC.put("userId", "user456");
        MDC.put("action", "test-warning");
        logger.warn("Warning log test: {}", message);
        MDC.clear();
        return "Warning log sent successfully";
    }

    @GetMapping("/logs/error")
    public String testErrorLog(@RequestParam(defaultValue = "Test error message") String message) {
        MDC.put("userId", "user789");
        MDC.put("action", "test-error");
        logger.error("Error log test: {}", message);
        MDC.clear();
        return "Error log sent successfully";
    }

    @GetMapping("/logs/debug")
    public String testDebugLog(@RequestParam(defaultValue = "Test debug message") String message) {
        MDC.put("userId", "user999");
        MDC.put("action", "test-debug");
        logger.debug("Debug log test: {}", message);
        MDC.clear();
        return "Debug log sent successfully";
    }

    @GetMapping("/logs/structured")
    public String testStructuredLog() {
        MDC.put("userId", "user100");
        MDC.put("sessionId", "session-xyz");
        MDC.put("requestId", "req-12345");
        MDC.put("operation", "structured-logging-test");
        
        logger.info("Starting structured logging test");
        logger.warn("This is a warning with structured data");
        logger.error("This is an error with structured context");
        
        MDC.clear();
        return "Structured logs sent successfully to Elasticsearch";
    }
}