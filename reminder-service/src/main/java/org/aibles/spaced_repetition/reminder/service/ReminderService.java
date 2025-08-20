package org.aibles.spaced_repetition.reminder.service;

import org.aibles.spaced_repetition.reminder.entity.Reminder;
import org.aibles.spaced_repetition.reminder.repository.ReminderRepository;
import org.aibles.spaced_repetition.shared.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ReminderService extends BaseServiceImpl<Reminder> {
    
    public ReminderService(ReminderRepository repository) {
        super(repository);
    }
    
    @Override
    protected String getEntityName() {
        return "Reminder";
    }
}