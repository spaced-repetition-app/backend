package org.aibles.spaced_repetition.user.service;

import org.aibles.spaced_repetition.user.entity.Friendship;
import org.aibles.spaced_repetition.user.repository.FriendshipRepository;
import org.aibles.spaced_repetition.shared.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FriendshipService extends BaseServiceImpl<Friendship> {
    
    public FriendshipService(FriendshipRepository repository) {
        super(repository);
    }
    
    @Override
    protected String getEntityName() {
        return "Friendship";
    }
}