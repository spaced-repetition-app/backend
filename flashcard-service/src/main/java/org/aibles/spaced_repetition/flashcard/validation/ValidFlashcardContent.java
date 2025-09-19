package org.aibles.spaced_repetition.flashcard.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FlashcardContentValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFlashcardContent {
    String message() default "Front and back content cannot be identical";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}