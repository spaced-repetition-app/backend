package org.aibles.spaced_repetition;

import io.sentry.Sentry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class SentryIntegrationTest {

    @Test
    public void testSentryExceptionCapture() {
        try {
            // Tạo exception để test
            throw new RuntimeException("Test exception from unit test");
        } catch (Exception e) {
            // Capture exception manually
            Sentry.captureException(e);
            System.out.println("Exception sent to Sentry: " + e.getMessage());
        }
    }

    @Test
    public void testSentryMessage() {
        // Test gửi message
        Sentry.captureMessage("Test message from unit test");
        System.out.println("Message sent to Sentry");
    }

    @Test
    public void testCustomException() {
        try {
            simulateBusinessLogicError();
        } catch (Exception e) {
            Sentry.captureException(e);
            System.out.println("Business logic exception sent to Sentry: " + e.getMessage());
        }
    }

    private void simulateBusinessLogicError() {
        throw new IllegalArgumentException("Invalid business parameter - test from unit test");
    }
}