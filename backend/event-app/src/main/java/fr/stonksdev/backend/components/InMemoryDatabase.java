package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.StonksEvent;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryDatabase {

    private Map<UUID, StonksEvent> events;

    public Map<UUID, StonksEvent> getEvents() {
        return events;
    }

    private Map<UUID, Activity> activities;

    public Map<UUID, Activity> getActivities() {
        return activities;
    }

    private Map<UUID, Room> rooms;

    public Map<UUID, Room> getRooms() {
        return rooms;
    }

    private Map<UUID, Set<Activity>> roomPlanning;

    public Map<UUID, Set<Activity>> getRoomPlanning() {
        return roomPlanning;
    }

    public InMemoryDatabase() {
        flush();
    }

    public void flush() {
        events = new HashMap<>();
        activities = new HashMap<>();
        rooms = new HashMap<>();
    }
}
