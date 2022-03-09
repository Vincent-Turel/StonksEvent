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

    void setNewActivityName(String newName, UUID eventId, String name);
    void setNewActivityBeginning(LocalDateTime newBeginning, UUID eventId, String name);
    void setNewActivityDuration(Duration newDuration, UUID eventId, String name);
    void setNewActivityDescription(String newDescription, UUID eventId, String name);
    void setNewActivityMaxPeopleAmount(int newMaxPeopleAmount, UUID eventId, String name);

    void deleteActivity(UUID eventId, String activityName);

    /*
    GETTER :
     */

    public int getNbEvent();
    public int getNbActivity();
    public StonksEvent getAnEvent(UUID eventId);
    public Map<UUID, StonksEvent> getAllEvent();
    public Activity getAnActivity(UUID eventId, String activityName);
    public Map<String, Activity> getAllActivities();
}
