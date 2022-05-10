package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.exceptions.AlreadyExistingActivityException;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityModifier {
    Activity createActivity(LocalDateTime beginning, Duration duration, String name, int maxPeopleAmount, StonksEvent stonksEvent) throws AlreadyExistingActivityException;

    Activity createActivity(LocalDateTime beginning, Duration duration, String name, StonksEvent stonksEvent) throws AlreadyExistingActivityException;

    Activity updateActivity(Long activityId, int maxPeopleAmount, LocalDateTime startDate, Duration duration) throws ActivityNotFoundException;

    void deleteActivity(Activity activity);

    List<Activity> getAllActivities();
}
