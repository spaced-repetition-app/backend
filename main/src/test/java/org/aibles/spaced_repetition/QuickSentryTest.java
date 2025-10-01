package org.aibles.spaced_repetition;

import io.sentry.Sentry;

public class QuickSentryTest {
    
    public static void main(String[] args) {
        // Cấu hình Sentry với DSN
        Sentry.init(options -> {
            options.setDsn("http://your-key@localhost:9100/1"); // Thay bằng DSN thật
            options.setDebug(true);
        });
        
        // Test gửi message
        Sentry.captureMessage("Test message from main method");
        
        // Test gửi exception
        try {
            throw new RuntimeException("Test exception from main method");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
        
        System.out.println("Sent test data to Sentry!");
        
        // Đợi một chút để Sentry gửi dữ liệu
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}