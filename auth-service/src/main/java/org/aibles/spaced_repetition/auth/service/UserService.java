package org.aibles.spaced_repetition.auth.service;

import org.aibles.spaced_repetition.auth.entity.User;
import org.aibles.spaced_repetition.auth.repository.UserRepository;
import org.aibles.spaced_repetition.shared.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseServiceImpl<User> {
    
    public UserService(UserRepository repository) {
        super(repository);
    }
    
    @Override
    protected String getEntityName() {
        return "User";
    }
}