package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.entities.StonksEvent;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Component
public class InMemoryDatabase {

    /*
    A faire : rajouté un map de type <room -> son id> on verra ça après la soutenance 
     */

    private Map<UUID, StonksEvent> events;

    public Map<UUID, StonksEvent> getEvents() {
        return events;
    }

    private Map<UUID, Activity> activities;

    public Map<UUID, Activity> getActivities() {
        return activities;
    }


    private Map<UUID, Set<Activity>> eventActivityAssociation;

    public Map<UUID, Set<Activity>> getEventActivityAssociation() {
        return eventActivityAssociation;
    }

    private Map<UUID, Room> rooms;

    public Map<UUID, Room> getRooms() {
        return rooms;
    }

    //roomID -> List of activityId for the corresponding room
    private Map<UUID, List<UUID>> roomPlanning;

    public Map<UUID, List<UUID>> getRoomPlanning() {
        return roomPlanning;
    }

    public InMemoryDatabase() {
        flush();
    }

    public void flush() {
        events = new HashMap<>();
        activities = new HashMap<>();
        eventActivityAssociation = new HashMap<>();
        rooms = new HashMap<>();
        roomPlanning = new HashMap<>();

        addHardcodedRooms();
    }

    // As an interim solution, we add hardcoded rooms to the database.
    private void addHardcodedRooms() {
        Room room314 = new Room("O+314", RoomKind.Classroom, 20);
        Room room106 = new Room("O+106", RoomKind.Classroom, 16);
        Room amphiForum = new Room("Amphi Forum", RoomKind.Amphitheatre, 200);
        Room amphi228 = new Room("O+228", RoomKind.Amphitheatre, 120);

        Stream.of(room314, room106, amphiForum, amphi228).forEach(room -> rooms.put(UUID.randomUUID(), room));
    }
}
