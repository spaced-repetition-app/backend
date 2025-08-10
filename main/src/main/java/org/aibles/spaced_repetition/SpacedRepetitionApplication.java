package org.aibles.spaced_repetition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {
    "org.aibles.spaced_repetition.auth",
    "org.aibles.spaced_repetition.user", 
    "org.aibles.spaced_repetition.flashcard",
    "org.aibles.spaced_repetition.review",
    "org.aibles.spaced_repetition.reminder",
    "org.aibles.spaced_repetition.core",
    "org.aibles.spaced_repetition.config"
})
@EntityScan(basePackages = {
    "org.aibles.spaced_repetition.auth.entity",
    "org.aibles.spaced_repetition.user.entity",
    "org.aibles.spaced_repetition.flashcard.entity", 
    "org.aibles.spaced_repetition.review.entity",
    "org.aibles.spaced_repetition.reminder.entity"
})
@EnableJpaRepositories(basePackages = {
    "org.aibles.spaced_repetition.auth.repository",
    "org.aibles.spaced_repetition.user.repository",
    "org.aibles.spaced_repetition.flashcard.repository",
    "org.aibles.spaced_repetition.review.repository", 
    "org.aibles.spaced_repetition.reminder.repository"
})
@EnableScheduling
@EnableTransactionManagement
public class SpacedRepetitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpacedRepetitionApplication.class, args);
    }

}