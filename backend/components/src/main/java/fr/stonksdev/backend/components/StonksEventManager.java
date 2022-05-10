package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.components.interfaces.Mail;
import fr.stonksdev.backend.components.interfaces.StonksEventFinder;
import fr.stonksdev.backend.components.interfaces.StonksEventModifier;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.components.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class StonksEventManager implements StonksEventModifier, StonksEventFinder {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private Mail mailProxy;

    @Override
    @Transactional
    public StonksEvent createEvent(String name, int maxPeopleAmount, LocalDateTime startDate, LocalDateTime endDate) throws AlreadyExistingEventException {
        StonksEvent newEvent = new StonksEvent(name, maxPeopleAmount, startDate, endDate);
        if (findByName(name).isPresent()) {
            throw new AlreadyExistingEventException(name);
        }
        eventRepo.save(newEvent);

        mailProxy.send("stonksdev.polyevent@gmail.com", "New event created",
                  "An event called " + name + " has been created.");
        return newEvent;
    }

    @Override
    @Transactional
    public StonksEvent updateEvent(Long eventId, int maxPeopleAmount, LocalDateTime startDate, LocalDateTime endDate) throws EventNotFoundException {
        Optional<StonksEvent> eventOpt = findById(eventId);
        if (eventOpt.isEmpty()) {
            throw new EventNotFoundException(eventId.toString());
        }
        StonksEvent event = eventOpt.get();
        event.setAmountOfPeople(maxPeopleAmount);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        return eventRepo.save(event);
    }

    @Override
    @Transactional
    public void deleteEvent(StonksEvent event) {
        eventRepo.delete(event);
    }

    @Override
    public List<StonksEvent> getAllEvent() {
        return eventRepo.findAll();
    }

    public Set<Activity> getAllActivitiesFromEvent(StonksEvent event) {
        return event.getActivities();
    }

    @Override
    public Optional<StonksEvent> findByName(String name) {
        return eventRepo.findStonksEventByName(name);
    }

    @Override
    public Optional<StonksEvent> findById(Long id) {
        return eventRepo.findById(id);
    }
}
