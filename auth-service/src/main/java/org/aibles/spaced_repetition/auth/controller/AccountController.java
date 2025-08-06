package org.aibles.spaced_repetition.auth.controller;

import org.aibles.spaced_repetition.auth.entity.Account;
import org.aibles.spaced_repetition.auth.service.AccountService;
import org.aibles.spaced_repetition.shared.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController extends BaseController<Account> {
    
    public AccountController(AccountService service) {
        super(service);
    }
    
    @Override
    protected String getEntityName() {
        return "Account";
    }
}