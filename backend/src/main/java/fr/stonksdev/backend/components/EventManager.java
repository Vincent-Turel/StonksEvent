package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.exceptions.EventIdNotFoundException;
import fr.stonksdev.backend.exceptions.ItemNotFoundException;
import fr.stonksdev.backend.interfaces.EventModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EventManager implements EventModifier {

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private InMemoryDatabase inMemoryDatabase;

    @Override
    public StonksEvent create(String name, int maxPeopleAmount, Date startDate, Date endDate) {
        return new StonksEvent(name, maxPeopleAmount, startDate, endDate);
    }

    @Override
    public boolean modify(String eventId, Activity newActivity) throws EventIdNotFoundException, ActivityNotFoundException {
        Optional<Activity> optAct = getActivitiesWithEvent(eventId).stream().filter(activity -> activity.getName().equals(newActivity.getName())).findFirst();

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

    List<Activity> activities(String eventName) throws ItemNotFoundException{
        Optional<StonksEvent> event = inMemoryDatabase.getEventList().stream().filter(ev -> ev.getName().equals(eventName)).findFirst();
        if (event.isPresent())
            return event.get().getActivities();
        throw new ItemNotFoundException("Event not found");
    }
    List<Room> getRequiredRoom(String eventName) throws ItemNotFoundException {
        return activities(eventName).stream().map(Activity::getRoom).collect(Collectors.toList());

    private void changeParamActivity(Activity oldActivity, Activity newActivity) {
        oldActivity.setBeginning(newActivity.getBeginning());
        oldActivity.setRoom(newActivity.getRoom());
        oldActivity.setDescription(newActivity.getDescription());
        oldActivity.setDuration(newActivity.getDuration());
        oldActivity.setMaxPeopleAmount(newActivity.getMaxPeopleAmount());
        oldActivity.setRequiredEquipment(newActivity.getRequiredEquipment());
    }

    public List<StonksEvent> getAllEvents() {
        return inMemoryDatabase.getEventList();
    }

    public List<Activity> getActivitiesWithEvent(String eventId) throws EventIdNotFoundException {
        Optional<StonksEvent> res = inMemoryDatabase.getEventList().stream().filter(stonksEvent -> stonksEvent.getId().equals(eventId)).findFirst();
        if (res.isPresent()) {
            return res.get().getActivities();
        }


        throw new EventIdNotFoundException();
    }

    List<Room> getRequiredRoom(String eventName) {
        StonksEvent event = inMemoryDatabase.getEventList().stream().filter(ev -> ev.getName().equals(eventName)).findFirst().get();
        return event.getActivities().stream().map(Activity::getRoom).collect(Collectors.toList());
    }


}
