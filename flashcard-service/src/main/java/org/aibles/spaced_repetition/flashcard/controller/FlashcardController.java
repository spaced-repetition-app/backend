package org.aibles.spaced_repetition.flashcard.controller;

import org.aibles.spaced_repetition.flashcard.entity.Flashcard;
import org.aibles.spaced_repetition.flashcard.service.FlashcardService;
import org.aibles.spaced_repetition.shared.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController extends BaseController<Flashcard> {
    
    public FlashcardController(FlashcardService service) {
        super(service);
    }
    
    @Override
    protected String getEntityName() {
        return "Flashcard";
    }
}