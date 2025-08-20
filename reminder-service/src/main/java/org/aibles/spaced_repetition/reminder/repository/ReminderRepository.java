package org.aibles.spaced_repetition.reminder.repository;

import org.aibles.spaced_repetition.reminder.entity.Reminder;
import org.aibles.spaced_repetition.shared.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends BaseRepository<Reminder> {
}