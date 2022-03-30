package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.exceptions.EventIdNotFoundException;
import fr.stonksdev.backend.interfaces.Mail;
import fr.stonksdev.backend.interfaces.StonksEventFinder;
import fr.stonksdev.backend.interfaces.StonksEventModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class StonksEventManager implements StonksEventModifier, StonksEventFinder {

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
    public StonksEvent createEvent(String name, int maxPeopleAmount, LocalDateTime startDate, LocalDateTime endDate) throws AlreadyExistingEventException {
        StonksEvent newEvent = new StonksEvent(name, maxPeopleAmount, startDate, endDate);
        if (findByNameFallible(name).isPresent()) {
            throw new AlreadyExistingEventException(name);
        }
        inMemoryDatabase.getEvents().put(newEvent.getId(), newEvent);
        eventIdList.add(newEvent.getId());

        mailProxy.send("stonksdev.polyevent@gmail.com", "New event created",
                  "An event called " + name + " has been created.");
        return newEvent;
    }

    @Override
    public StonksEvent updateEvent(UUID eventId, int maxPeopleAmount, LocalDateTime startDate, LocalDateTime endDate) throws EventIdNotFoundException {
        StonksEvent event = findById(eventId);

        event.setAmountOfPeople(maxPeopleAmount);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        return event;
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
    public Activity createActivity(LocalDateTime beginning, Duration duration, String name, String description, int maxPeopleAmount, UUID eventId) {
        Activity newActivity = new Activity(beginning, duration, name, description, maxPeopleAmount, eventId);
        createActivity(newActivity, eventId);
        return newActivity;
    }

    @Override
    public Activity createActivity(LocalDateTime beginning, Duration duration, String name, int maxPeopleAmount, UUID eventId) {
        Activity newActivity = new Activity(beginning, duration, name, maxPeopleAmount, eventId);
        createActivity(newActivity, eventId);
        return newActivity;
    }

    @Override
    public Activity createActivity(LocalDateTime beginning, Duration duration, String name, UUID eventId) {
        Activity newActivity = new Activity(beginning, duration, name, eventId);
        createActivity(newActivity, eventId);
        return newActivity;
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
            //TODO: put activity in an event
            if (!inMemoryDatabase.getEventActivityAssociation().containsKey(eventId)) {
                inMemoryDatabase.getEventActivityAssociation().put(eventId, new HashSet<>());
            }

            inMemoryDatabase.getEventActivityAssociation().get(eventId).add(newActivity);

            activitiesId.add(newActivity.getActivityID());
        }
    }

    /*
    MODIFY AN ACTIVITY
     */

    @Override
    public Activity updateActivity(Activity activity, int maxPeopleAmount, LocalDateTime startDate, Duration duration) {
        activity.setMaxPeopleAmount(maxPeopleAmount);
        activity.setBeginning(startDate);
        activity.setDuration(duration);
        return activity;
    }

    /*
    DELETE AN ACTIVITY
     */
    @Override
    public void deleteActivity(Activity activity) {
        UUID activityId = activity.getActivityID();

        inMemoryDatabase.getActivities().remove(activityId);
        activitiesId.remove(activityId);
    }

    /*
    GETTER :
     */

    public StonksEvent getAnEvent(UUID eventId) throws EventIdNotFoundException {
        StonksEvent event = getAllEvent().get(eventId);
        if (event == null) {
            throw new EventIdNotFoundException("No such event: " + eventId);
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

    public Set<Activity> getAllActivitiesFromEvent(UUID eventId) {
        return inMemoryDatabase.getEventActivityAssociation().get(eventId);
    }

    public List<UUID> getEventIdList() {
        return eventIdList;
    }

    public List<UUID> getActivitiesId() {
        return activitiesId;
    }

    @Override
    public Optional<StonksEvent> findByNameFallible(String name) {
        return inMemoryDatabase
                .getEvents()
                .values()
                .stream()
                .filter(stonksEvent -> stonksEvent.getName().equals(name))
                .findFirst();
    }

    @Override
    public StonksEvent findByName(String name) throws EventIdNotFoundException {
        return findByNameFallible(name).orElseThrow(EventIdNotFoundException::new);
    }

    @Override
    public Optional<StonksEvent> findByIdFallible(UUID id) {
        StonksEvent event = inMemoryDatabase.getEvents().get(id);
        return Optional.ofNullable(event);
    }

    @Override
    public StonksEvent findById(UUID id) throws EventIdNotFoundException {
        return findByIdFallible(id).orElseThrow(EventIdNotFoundException::new);
    }

    private Optional<Activity> findActivityById(UUID id) {
        Activity activity = inMemoryDatabase.getActivities().get(id);
        return Optional.ofNullable(activity);
    }

    public void reset() {
        this.inMemoryDatabase.getEvents().clear();
        this.inMemoryDatabase.getActivities().clear();
        this.eventIdList = new ArrayList<>();
        this.activitiesId = new ArrayList<>();
    }
}
