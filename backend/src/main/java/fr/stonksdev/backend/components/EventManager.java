package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.ItemNotFoundException;
import fr.stonksdev.backend.interfaces.EventModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
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
    public boolean modify(StonksEvent eventToModify, StonksEvent eventModified) throws ItemNotFoundException {
        return false;
    }

    @Override
    public boolean delete(StonksEvent eventToDelete) throws ItemNotFoundException {
        return false;
    }

    List<Room> getRequiredRoom(String eventName) {
        StonksEvent event = inMemoryDatabase.getEventList().stream().filter(ev -> ev.getName().equals(eventName)).findFirst().get();
        return event.getActivities().stream().map(Activity::getRoom).collect(Collectors.toList());
    }
}
