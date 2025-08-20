package org.aibles.spaced_repetition.reminder.controller;

import org.aibles.spaced_repetition.reminder.entity.Reminder;
import org.aibles.spaced_repetition.reminder.service.ReminderService;
import org.aibles.spaced_repetition.shared.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController extends BaseController<Reminder> {
    
    public ReminderController(ReminderService service) {
        super(service);
    }
    
    @Override
    protected String getEntityName() {
        return "Reminder";
    }
}