package org.aibles.spaced_repetition.service;

import io.sentry.Sentry;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestSentryService {

    public void testExceptionCapture() {
        try {
            throw new RuntimeException("Test exception from service layer");
        } catch (Exception e) {
            //log.error("Exception occurred in service", e);
            Sentry.captureException(e);
        }
    }

    public void testWithUserContext() {
        Sentry.configureScope(scope -> {
            io.sentry.protocol.User user = new io.sentry.protocol.User();
            user.setId("123");
            user.setEmail("test@example.com");
            user.setUsername("testuser");
            
            scope.setUser(user);
            scope.setTag("environment", "testing");
            scope.setTag("feature", "sentry-integration");
        });

        try {
            throw new IllegalStateException("Test exception with user context");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }

    public void testCustomBreadcrumbs() {
        Sentry.addBreadcrumb("Starting complex operation");
        Sentry.addBreadcrumb("Validating input parameters");
        Sentry.addBreadcrumb("Processing business logic");
        
        try {
            throw new Exception("Error during complex operation");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }
}