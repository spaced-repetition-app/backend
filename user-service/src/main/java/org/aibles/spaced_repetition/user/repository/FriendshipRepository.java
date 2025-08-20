package org.aibles.spaced_repetition.user.repository;

import org.aibles.spaced_repetition.user.entity.Friendship;
import org.aibles.spaced_repetition.shared.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends BaseRepository<Friendship> {
}