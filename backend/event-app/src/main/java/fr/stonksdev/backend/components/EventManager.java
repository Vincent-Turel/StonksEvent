package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.exceptions.EventIdNotFoundException;
import fr.stonksdev.backend.exceptions.ItemNotFoundException;
import fr.stonksdev.backend.interfaces.EventModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class EventManager implements EventModifier {
    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private InMemoryDatabase inMemoryDatabase;

    @Override
    public StonksEvent create(String name, int maxPeopleAmount, LocalDateTime start, Duration duration) {
        StonksEvent stonksEvent = new StonksEvent(name, maxPeopleAmount, start, duration);
        inMemoryDatabase.incrementEvents();
        inMemoryDatabase.getEvents().put(stonksEvent.getId(), stonksEvent);
        return stonksEvent;
    }

    @Override
    public boolean modify(String eventId, Activity newActivity) throws EventIdNotFoundException, ActivityNotFoundException {
        Optional<Activity> optAct = getActivitiesWithEvent(eventId)
                .stream()
                .filter(activity -> activity.getName().equals(newActivity.getName()))
                .findFirst();

        if (optAct.isEmpty()) {
            throw new ActivityNotFoundException(newActivity.getName());
        }

        changeParamActivity(optAct.get(), newActivity);
        return true;
    }

    @Override
    public boolean delete(StonksEvent eventToDelete) throws ItemNotFoundException {
        return false;
    }

    Set<Activity> activities(String eventName) throws ItemNotFoundException {
        Optional<StonksEvent> event = inMemoryDatabase
                .getEvents()
                .values()
                .stream()
                .filter(ev -> ev.getName().equals(eventName))
                .findFirst();

        if (event.isPresent())
            return inMemoryDatabase.getActivities().get(event.get());

        throw new ItemNotFoundException("Event not found");
    }

    // TODO(danlux18): fix this horrible mess.
    List<Room> getRequiredRoom(String eventName) throws ItemNotFoundException {
        // return activities(eventName).stream().map(Activity::getRoom).collect(Collectors.toList());
        return null;
    }

    private void changeParamActivity(Activity oldActivity, Activity newActivity) {
        oldActivity.setBeginning(newActivity.getBeginning());
        oldActivity.setDescription(newActivity.getDescription());
        oldActivity.setDuration(newActivity.getDuration());
        oldActivity.setMaxPeopleAmount(newActivity.getMaxPeopleAmount());
    }

    public Set<StonksEvent> getAllEvents() {
        return new HashSet<>(inMemoryDatabase.getEvents().values());
    }

    public Set<Activity> getActivitiesWithEvent(String eventId) throws EventIdNotFoundException {
        if (!inMemoryDatabase.getEvents().containsKey(eventId)) {
            throw new EventIdNotFoundException();
        }
        return inMemoryDatabase.getActivities().get(inMemoryDatabase.getEvents().get(eventId));
    }
}
