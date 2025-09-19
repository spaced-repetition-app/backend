package org.aibles.spaced_repetition.flashcard.exception;

import org.aibles.spaced_repetition.shared.exception.BusinessException;

public class FlashcardBusinessException extends BusinessException {

    public FlashcardBusinessException(String code, String message) {
        super(code, message);
    }

    public FlashcardBusinessException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public static FlashcardBusinessException deckNotFound(String deckId) {
        return new FlashcardBusinessException("DECK_NOT_FOUND",
                "Deck with ID '" + deckId + "' not found");
    }

    public static FlashcardBusinessException deckNotAccessible(String deckId, String userId) {
        return new FlashcardBusinessException("DECK_NOT_ACCESSIBLE",
                "User '" + userId + "' does not have access to deck '" + deckId + "'");
    }

    public static FlashcardBusinessException duplicateFlashcard(String front, String back) {
        return new FlashcardBusinessException("DUPLICATE_FLASHCARD",
                "Flashcard with this front and back content already exists in the deck");
    }

    public static FlashcardBusinessException deckCapacityExceeded(String deckId, int maxCapacity) {
        return new FlashcardBusinessException("DECK_CAPACITY_EXCEEDED",
                "Deck '" + deckId + "' has reached maximum capacity of " + maxCapacity + " flashcards");
    }
}