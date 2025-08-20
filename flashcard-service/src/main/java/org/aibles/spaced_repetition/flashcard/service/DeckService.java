package org.aibles.spaced_repetition.flashcard.service;

import org.aibles.spaced_repetition.flashcard.entity.Deck;
import org.aibles.spaced_repetition.flashcard.repository.DeckRepository;
import org.aibles.spaced_repetition.shared.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DeckService extends BaseServiceImpl<Deck> {
    
    public DeckService(DeckRepository repository) {
        super(repository);
    }
    
    @Override
    protected String getEntityName() {
        return "Deck";
    }
}