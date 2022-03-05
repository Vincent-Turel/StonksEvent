package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.entities.StonksEvent;
import org.springframework.stereotype.Component;

import java.util.*;

import static fr.stonksdev.backend.entities.RoomKind.*;

@Component
public class InMemoryDatabase {
    private int eventCounter = 0;

    private Map<String, StonksEvent> events;
    public Map<String, StonksEvent> getEvents() {
        return events;
    }

    private Map<StonksEvent, Set<Activity>> activities;
    public Map<StonksEvent, Set<Activity>> getActivities() {
        return activities;
    }

    private Map<Room, Set<Activity>> rooms;
    public Map<Room, Set<Activity>> getRooms() {
        return rooms;
    }

    private List<Room> roomList;

    private void populateRoom() {
        roomList = new ArrayList<>();
        roomList.add(new Room("O+304", Demonstration,36));
        roomList.add(new Room("O+305", Demonstration,34));
        roomList.add(new Room("O+306", Demonstration,32));
        roomList.add(new Room("E+100", Demonstration,36));
        roomList.add(new Room("E+102", Demonstration,30));
        roomList.add(new Room("A1", Amphitheatre,80));
        roomList.add(new Room("Amphi forum", Amphitheatre,90));
        roomList.add(new Room("Cafétéria", Lunch,50));
        roomList.add(new Room("R.U.", Lunch,200));
        for (Room room: roomList) {
            rooms.put(room,new HashSet<>());
        }
    }

    public void incrementEvents() {
        eventCounter++;
    }

    public InMemoryDatabase() {
        flush();
    }

    public void flush() {
        events = new HashMap<>();
        activities = new HashMap<>();
        rooms = new HashMap<>();
        populateRoom();
        eventCounter = 0;
    }
}
