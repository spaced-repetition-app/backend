package org.aibles.spaced_repetition.auth.controller;

import org.aibles.spaced_repetition.auth.entity.User;
import org.aibles.spaced_repetition.auth.service.UserService;
import org.aibles.spaced_repetition.shared.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController<User> {
    
    public UserController(UserService service) {
        super(service);
    }
    
    @Override
    protected String getEntityName() {
        return "User";
    }
}