package org.aibles.spaced_repetition.flashcard.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DeckExistsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDeckExists {
    String message() default "Deck does not exist or is not accessible";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}