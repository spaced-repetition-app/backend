package org.aibles.spaced_repetition.flashcard.repository;

import org.aibles.spaced_repetition.flashcard.entity.Deck;
import org.aibles.spaced_repetition.shared.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckRepository extends BaseRepository<Deck> {
}