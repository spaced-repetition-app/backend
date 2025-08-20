package org.aibles.spaced_repetition.flashcard.service;

import org.aibles.spaced_repetition.flashcard.entity.Flashcard;
import org.aibles.spaced_repetition.flashcard.repository.FlashcardRepository;
import org.aibles.spaced_repetition.shared.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FlashcardService extends BaseServiceImpl<Flashcard> {
    
    public FlashcardService(FlashcardRepository repository) {
        super(repository);
    }
    
    @Override
    protected String getEntityName() {
        return "Flashcard";
    }
}