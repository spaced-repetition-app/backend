package org.aibles.spaced_repetition;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpacedRepetitionApplication {
    
    private static final Logger log = LoggerFactory.getLogger(SpacedRepetitionApplication.class);

    public static void main(String[] args) {
        log.info("Starting Spaced Repetition Application...");
        SpringApplication.run(SpacedRepetitionApplication.class, args);
        log.info("Spaced Repetition Application started successfully!");
    }

}