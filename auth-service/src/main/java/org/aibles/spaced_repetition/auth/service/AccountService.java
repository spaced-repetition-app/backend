package org.aibles.spaced_repetition.auth.service;

import org.aibles.spaced_repetition.auth.entity.Account;
import org.aibles.spaced_repetition.auth.repository.AccountRepository;
import org.aibles.spaced_repetition.shared.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AccountService extends BaseServiceImpl<Account> {
    
    public AccountService(AccountRepository repository) {
        super(repository);
    }
    
    @Override
    protected String getEntityName() {
        return "Account";
    }
}