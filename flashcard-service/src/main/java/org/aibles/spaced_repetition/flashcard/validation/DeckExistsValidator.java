package org.aibles.spaced_repetition.flashcard.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.aibles.spaced_repetition.flashcard.service.DeckService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeckExistsValidator implements ConstraintValidator<ValidDeckExists, String> {

    private final DeckService deckService;

    @Override
    public boolean isValid(String deckId, ConstraintValidatorContext context) {
        if (deckId == null || deckId.trim().isEmpty()) {
            return false;
        }

        try {
            return deckService.existsById(deckId);
        } catch (Exception e) {
            return false;
        }
    }
}