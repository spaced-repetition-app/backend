package org.aibles.spaced_repetition.flashcard.service;

import org.aibles.spaced_repetition.flashcard.dto.CreateDeckRequest;
import org.aibles.spaced_repetition.flashcard.dto.DeckDto;
import org.aibles.spaced_repetition.flashcard.entity.Deck;
import org.aibles.spaced_repetition.flashcard.repository.DeckRepository;
import org.aibles.spaced_repetition.shared.service.BaseServiceImpl;
import org.springframework.cache.annotation.Cacheable;
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

    @Override
    @Cacheable(value = "deck-exists-cache", key = "#id")
    public boolean existsById(String id) {
        return super.existsById(id);
    }

    public DeckDto createDeck(CreateDeckRequest request) {
        Deck deck = new Deck();
        deck.setTitle(request.getTitle());
        deck.setDescription(request.getDescription());
        deck.setOwnerId(request.getOwnerId());

        Deck savedDeck = create(deck);
        return mapToDto(savedDeck);
    }

    private DeckDto mapToDto(Deck deck) {
        DeckDto dto = new DeckDto();
        dto.setId(deck.getId());
        dto.setTitle(deck.getTitle());
        dto.setDescription(deck.getDescription());
        dto.setOwnerId(deck.getOwnerId());

        dto.setCreatedBy(deck.getCreatedBy());
        dto.setCreatedAt(deck.getCreatedAt());
        dto.setLastModifiedBy(deck.getUpdatedBy());
        dto.setLastModifiedAt(deck.getUpdatedAt());
        return dto;
    }
}