package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.StonksEvent;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public interface StonksEventModifier {
    /*
    CREATE/MODIFY AN EVENT :
     */
    void createEvent(String name, int maxPeopleAmount, LocalDateTime startDate, LocalDateTime endDate);

    void setNewEventName(String newName, UUID eventId);
    void setNewAmount(int newMaxPeopleAmount, UUID eventId);

    void deleteEvent(UUID eventID);

    /*
    CREATE/MODIFY AN ACTIVITY :
     */
    void createActivity(LocalDateTime beginning, Duration duration, String name, String description, int maxPeopleAmount, UUID eventId);
    void createActivity(LocalDateTime beginning, Duration duration, String name, int maxPeopleAmount, UUID eventId);
    void createActivity(LocalDateTime beginning, Duration duration, String name, UUID eventId);

    void setNewActivityName(String newName, UUID activityId);
    void setNewActivityBeginning(LocalDateTime newBeginning, UUID activityId);
    void setNewActivityDuration(Duration newDuration, UUID activityId);
    void setNewActivityDescription(String newDescription, UUID activityId);
    void setNewActivityMaxPeopleAmount(int newMaxPeopleAmount, UUID activityId);

    void deleteActivity(UUID activityId);

    /*
    GETTER :
     */
    public StonksEvent getAnEvent(UUID eventId);
    public Map<UUID, StonksEvent> getAllEvent();
    public Activity getAnActivity(UUID activityId);
    public Map<UUID, Activity> getAllActivities();
    public void reset();
}
