package org.aibles.spaced_repetition.user.controller;

import org.aibles.spaced_repetition.user.entity.Friendship;
import org.aibles.spaced_repetition.user.service.FriendshipService;
import org.aibles.spaced_repetition.shared.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipController extends BaseController<Friendship> {
    
    public FriendshipController(FriendshipService service) {
        super(service);
    }
    
    @Override
    protected String getEntityName() {
        return "Friendship";
    }
}