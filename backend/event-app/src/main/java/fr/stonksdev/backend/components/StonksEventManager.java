package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.interfaces.StonksEventModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class StonksEventManager implements StonksEventModifier {

    @Autowired
    private InMemoryDatabase inMemoryDatabase;

    private final List<UUID> eventIdList = new ArrayList<>();
    private final List<String> activitiesName = new ArrayList<>();

    /*
    CREATE/MODIFY AN EVENT :
     */
    @Override
    public void createEvent(String name, int maxPeopleAmount, LocalDateTime startDate, LocalDateTime endDate) {
        eventIdList.add(inMemoryDatabase.addEvent(new StonksEvent(name, maxPeopleAmount, startDate, endDate)));
    }

    @Override
    public void setNewEventName(String newName, UUID eventId) {
        inMemoryDatabase.getEvents().get(eventId).setName(newName);
    }

    @Override
    public void setNewAmount(int newMaxPeopleAmount, UUID eventId) {
        inMemoryDatabase.getEvents().get(eventId).setAmountOfPeople(newMaxPeopleAmount);
    }

    /*
    DELETE AN EVENT
     */
    @Override
    public void deleteEvent(UUID eventID) {
        List<String> remove = inMemoryDatabase.deleteEvent(eventID);
        if (!remove.isEmpty()) {
            for (String value : remove) {
                activitiesName.remove(value);
            }
        }
        eventIdList.remove(eventID);
    }

    /*
    CREATE AN ACTIVITY
     */
    @Override
    public void createActivity(LocalDateTime beginning, Duration duration, String name, String description, int maxPeopleAmount, UUID eventUUID) {
        if (inMemoryDatabase.addActivity(new Activity(beginning, duration, name, description, maxPeopleAmount, eventUUID))) {
            activitiesName.add(name);
        }
    }

    @Override
    public void createActivity(LocalDateTime beginning, Duration duration, String name, int maxPeopleAmount, UUID eventUUID) {
        if (inMemoryDatabase.addActivity(new Activity(beginning, duration, name, maxPeopleAmount, eventUUID))) {
            activitiesName.add(name);
        }
    }

    @Override
    public void createActivity(LocalDateTime beginning, Duration duration, String name, UUID eventUUID) {
        if (inMemoryDatabase.addActivity(new Activity(beginning, duration, name, eventUUID))) {
            activitiesName.add(name);
        }
    }

    /*
    MODIFY AN ACTIVITY
     */
    @Override
    public void setNewActivityName(String newName, UUID eventId, String name) {
        inMemoryDatabase.getActivities().get(eventId.toString() + name).setName(newName);
    }

    @Override
    public void setNewActivityBeginning(LocalDateTime newBeginning, UUID eventId, String name) {
        inMemoryDatabase.getActivities().get(eventId.toString() + name).setBeginning(newBeginning);
    }

    @Override
    public void setNewActivityDuration(Duration newDuration, UUID eventId, String name) {
        inMemoryDatabase.getActivities().get(eventId.toString() + name).setDuration(newDuration);
    }

    @Override
    public void setNewActivityDescription(String newDescription, UUID eventId, String name) {
        inMemoryDatabase.getActivities().get(eventId.toString() + name).setDescription(newDescription);
    }

    @Override
    public void setNewActivityMaxPeopleAmount(int newMaxPeopleAmount, UUID eventId, String name) {
        inMemoryDatabase.getActivities().get(eventId.toString() + name).setMaxPeopleAmount(newMaxPeopleAmount);
    }

    /*
    DELETE AN ACTIVITY
     */
    @Override
    public void deleteActivity(UUID eventId, String activityName) {
        inMemoryDatabase.deleteActivity(eventId.toString() + activityName);
        activitiesName.remove(activityName);
    }

    /*
    GETTER :
     */
    public int getNbEvent() {
        return inMemoryDatabase.getEventCounter();
    }

    public int getNbActivity() {
        return inMemoryDatabase.getActivityCounter();
    }

    public StonksEvent getAnEvent(UUID eventId) {
        return inMemoryDatabase.getEvents().get(eventId);
    }

    public Map<UUID, StonksEvent> getAllEvent() {
        return inMemoryDatabase.getEvents();
    }

    public Activity getAnActivity(UUID eventId, String activityName) {
        return inMemoryDatabase.getActivities().get(eventId.toString() + activityName);
    }

    public Map<String, Activity> getAllActivities() {
        return inMemoryDatabase.getActivities();
    }

    public List<UUID> getEventIdList() {
        return eventIdList;
    }

    public List<String> getActivitiesName() {
        return activitiesName;
    }
}
