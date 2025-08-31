package org.aibles.spaced_repetition.flashcard.controller;

import org.aibles.spaced_repetition.flashcard.entity.Deck;
import org.aibles.spaced_repetition.flashcard.service.DeckService;
import org.aibles.spaced_repetition.shared.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/decks")
public class DeckController extends BaseController<Deck> {
    
    public DeckController(DeckService service) {
        super(service);
    }
    
    @Override
    protected String getEntityName() {
        return "Deck";
    }
}