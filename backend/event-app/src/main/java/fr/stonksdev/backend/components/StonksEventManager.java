package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.interfaces.Mail;
import fr.stonksdev.backend.interfaces.StonksEventModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class StonksEventManager implements StonksEventModifier {

    @Autowired
    private InMemoryDatabase inMemoryDatabase;

    @Autowired
    private Mail mailProxy;

    private List<UUID> eventIdList = new ArrayList<>();
    private List<UUID> activitiesId = new ArrayList<>();

    /*
    CREATE/MODIFY AN EVENT :
     */
    @Override
    public void createEvent(String name, int maxPeopleAmount, LocalDateTime startDate, LocalDateTime endDate) {
        StonksEvent newEvent = new StonksEvent(name, maxPeopleAmount, startDate, endDate);
        inMemoryDatabase.getEvents().put(newEvent.getId(), newEvent);
        eventIdList.add(newEvent.getId());
        mailProxy.send("stonksdev.polyevent@gmail.com", "New event created",
                  "An event called " + name + "has been created.");
    }

    @Override
    public void setNewEventName(String newName, UUID eventId) {
        getAnEvent(eventId).setName(newName);
    }

    @Override
    public void setNewAmount(int newMaxPeopleAmount, UUID eventId) {
        getAnEvent(eventId).setAmountOfPeople(newMaxPeopleAmount);
    }

    /*
    DELETE AN EVENT
     */
    @Override
    public void deleteEvent(UUID eventId) {
        if (!inMemoryDatabase.getActivities().isEmpty()) {
            Iterator<Map.Entry<UUID, Activity>> it = inMemoryDatabase.getActivities().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<UUID, Activity> item = it.next();
                if (item.getValue().getEventId().equals(eventId)) {
                    activitiesId.remove(item.getValue().getActivityID());
                    it.remove();
                }
            }
        }
        inMemoryDatabase.getEvents().remove(eventId);
        eventIdList.remove(eventId);
    }

    /*
    CREATE AN ACTIVITY
     */
    @Override
    public void createActivity(LocalDateTime beginning, Duration duration, String name, String description, int maxPeopleAmount, UUID eventId) {
        Activity newActivity = new Activity(beginning, duration, name, description, maxPeopleAmount, eventId);
        createActivity(newActivity, eventId);
    }

    @Override
    public void createActivity(LocalDateTime beginning, Duration duration, String name, int maxPeopleAmount, UUID eventId) {
        Activity newActivity = new Activity(beginning, duration, name, maxPeopleAmount, eventId);
        createActivity(newActivity, eventId);
    }

    @Override
    public void createActivity(LocalDateTime beginning, Duration duration, String name, UUID eventId) {
        Activity newActivity = new Activity(beginning, duration, name, eventId);
        createActivity(newActivity, eventId);
    }

    private void createActivity(Activity newActivity, UUID eventId) {
        boolean alreadyExist = false;
        if (!inMemoryDatabase.getActivities().isEmpty()) {
            for (Map.Entry<UUID, Activity> item : inMemoryDatabase.getActivities().entrySet()) {
                if (item.getValue().getEventId().equals(eventId) && item.getValue().getName().equals(newActivity.getName())) {
                    alreadyExist = true;
                    break;
                }
            }
        }
        if (!alreadyExist) {
            inMemoryDatabase.getActivities().put(newActivity.getActivityID(), newActivity);
            activitiesId.add(newActivity.getActivityID());
        }
    }

    /*
    MODIFY AN ACTIVITY
     */
    @Override
    public void setNewActivityName(String newName, UUID activityId) {
        inMemoryDatabase.getActivities().get(activityId).setName(newName);
    }

    @Override
    public void setNewActivityBeginning(LocalDateTime newBeginning, UUID activityId) {
        inMemoryDatabase.getActivities().get(activityId).setBeginning(newBeginning);
    }

    @Override
    public void setNewActivityDuration(Duration newDuration, UUID activityId) {
        inMemoryDatabase.getActivities().get(activityId).setDuration(newDuration);
    }

    @Override
    public void setNewActivityDescription(String newDescription, UUID activityId) {
        inMemoryDatabase.getActivities().get(activityId).setDescription(newDescription);
    }

    @Override
    public void setNewActivityMaxPeopleAmount(int newMaxPeopleAmount, UUID activityId) {
        inMemoryDatabase.getActivities().get(activityId).setMaxPeopleAmount(newMaxPeopleAmount);
    }

    /*
    DELETE AN ACTIVITY
     */
    @Override
    public void deleteActivity(UUID activityId) {
        inMemoryDatabase.getActivities().remove(activityId);
        activitiesId.remove(activityId);
    }

    /*
    GETTER :
     */

    public StonksEvent getAnEvent(UUID eventId) {
        StonksEvent event = getAllEvent().get(eventId);

        if (Objects.isNull(event)) {
            throw new NoSuchElementException("No such event: " + eventId);
        }

        return event;
    }

    public Map<UUID, StonksEvent> getAllEvent() {
        return inMemoryDatabase.getEvents();
    }

    public Activity getAnActivity(UUID activityId) {
        Activity activity = inMemoryDatabase.getActivities().get(activityId);
        if (Objects.isNull(activity)) {
            throw new NoSuchElementException("No such activity: " + activityId);
        }
        return activity;
    }

    public Map<UUID, Activity> getAllActivities() {
        return inMemoryDatabase.getActivities();
    }

    public List<UUID> getEventIdList() {
        return eventIdList;
    }

    public List<UUID> getActivitiesId() {
        return activitiesId;
    }

    public void reset() {
        this.inMemoryDatabase.getEvents().clear();
        this.inMemoryDatabase.getActivities().clear();
        this.eventIdList = new ArrayList<>();
        this.activitiesId = new ArrayList<>();
    }
}
