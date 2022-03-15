package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.exceptions.EventIdNotFoundException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public interface StonksEventModifier {
    /*
    CREATE/MODIFY AN EVENT :
     */
    StonksEvent createEvent(String name, int maxPeopleAmount, LocalDateTime startDate, LocalDateTime endDate) throws AlreadyExistingEventException;

    StonksEvent updateEvent(UUID eventId, int maxPeopleAmount, LocalDateTime startDate, LocalDateTime endDate) throws EventIdNotFoundException;

    void deleteEvent(UUID eventID);

    /*
    CREATE/MODIFY AN ACTIVITY :
     */
    Activity createActivity(LocalDateTime beginning, Duration duration, String name, String description, int maxPeopleAmount, UUID eventId);
    Activity createActivity(LocalDateTime beginning, Duration duration, String name, int maxPeopleAmount, UUID eventId);
    Activity createActivity(LocalDateTime beginning, Duration duration, String name, UUID eventId);

    Activity updateActivity(UUID activityId, int maxPeopleAmount, LocalDateTime startDate, Duration duration) throws ActivityNotFoundException;

    void deleteActivity(UUID activityId);

    /*
    GETTER :
     */
    StonksEvent getAnEvent(UUID eventId) throws EventIdNotFoundException;
    Map<UUID, StonksEvent> getAllEvent();
    Activity getAnActivity(UUID activityId);
    Map<UUID, Activity> getAllActivities();
    void reset();
}
