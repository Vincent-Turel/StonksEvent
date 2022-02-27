package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.ItemNotFoundException;
import fr.stonksdev.backend.interfaces.EventModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventManager implements EventModifier {

    @Autowired
    public ActivityManager activityManager;


    @Override
    public StonksEvent create(String name, int maxPeopleAmount, List<Activity> activities) {
        return new StonksEvent(name, maxPeopleAmount, activities);
    }

    @Override
    public boolean modify(StonksEvent eventToModify, StonksEvent eventModified) throws ItemNotFoundException {
        return false;
    }

    @Override
    public boolean delete(StonksEvent eventToDelete) throws ItemNotFoundException {
        return false;
    }

    List<Room> getRequiredRoom(String eventName) {
        /*
        StonksEvent event = getEvent().findFirst((ev) -> ev.getName() == eventName);
        return event.getActivities().stream().map(act -> act::getRoom()).collect(Collectors.toList());
         */
        throw new RuntimeException();
    }
}
