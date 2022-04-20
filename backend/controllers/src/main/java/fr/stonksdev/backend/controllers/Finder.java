package fr.stonksdev.backend.controllers;

import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.components.interfaces.ActivityFinder;
import fr.stonksdev.backend.components.interfaces.RoomFinder;
import fr.stonksdev.backend.components.interfaces.StonksEventFinder;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.StonksEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Finder {
    @Autowired
    private StonksEventFinder eventFinder;

    @Autowired
    private ActivityFinder activityFinder;

    @Autowired
    private RoomFinder roomFinder;

    public StonksEvent retrieveEvent(Long eventId) throws EventNotFoundException {
        Optional<StonksEvent> eventOptional = eventFinder.findById(eventId);
        if (eventOptional.isEmpty()) {
            throw new EventNotFoundException(eventId.toString());
        }
        return eventOptional.get();
    }

    public StonksEvent retrieveEvent(String eventName) throws EventNotFoundException {
        Optional<StonksEvent> eventOptional = eventFinder.findByName(eventName);
        if (eventOptional.isEmpty()) {
            throw new EventNotFoundException(eventName);
        }
        return eventOptional.get();
    }

    public Activity retrieveActivity(Long activityId) throws ActivityNotFoundException {
        Optional<Activity> activityOptional = activityFinder.findById(activityId);
        if (activityOptional.isEmpty()) {
            throw new ActivityNotFoundException(activityId.toString());
        }
        return activityOptional.get();
    }

    public Activity retrieveActivity(String activityName) throws ActivityNotFoundException {
        Optional<Activity> activityOptional = activityFinder.findByName(activityName);
        if (activityOptional.isEmpty()) {
            throw new ActivityNotFoundException(activityName);
        }
        return activityOptional.get();
    }

    public Room retrieveRoom(Long roomId) throws RoomNotFoundException {
        Optional<Room> roomOptional = roomFinder.findById(roomId);
        if (roomOptional.isEmpty()) {
            throw new RoomNotFoundException(roomId.toString());
        }
        return roomOptional.get();
    }

    public Room retrieveRoom(String roomName) throws RoomNotFoundException {
        Optional<Room> roomOptional = roomFinder.findByName(roomName);
        if (roomOptional.isEmpty()) {
            throw new RoomNotFoundException(roomName);
        }
        return roomOptional.get();
    }
}
