package org.aibles.spaced_repetition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SpacedRepetitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpacedRepetitionApplication.class, args);
    }

}