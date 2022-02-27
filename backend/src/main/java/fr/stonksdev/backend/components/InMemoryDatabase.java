package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.entities.StonksEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static fr.stonksdev.backend.entities.RoomKind.*;

@Component
public class InMemoryDatabase {
    private int eventCounter = 0;
    private List<StonksEvent> eventList;
    private List<Room> roomList = populateRoom();

    private List<Room> populateRoom() {
        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(new Room("O+304", Demonstration,36));
        rooms.add(new Room("O+305", Demonstration,34));
        rooms.add(new Room("O+306", Demonstration,32));
        rooms.add(new Room("E+100", Demonstration,36));
        rooms.add(new Room("E+102", Demonstration,30));
        rooms.add(new Room("A1", Amphitheatre,80));
        rooms.add(new Room("Amphi forum", Amphitheatre,90));
        rooms.add(new Room("Cafétéria", Lunch,50));
        rooms.add(new Room("R.U.", Lunch,200));
        return rooms;
    }
    List<StonksEvent> getEventList() {return eventList;}

    public void incrementEvents() { eventCounter++; }
    public int howManyEvents() { return eventCounter; }

    public InMemoryDatabase() {
        flush();
    }

    public void flush() {
        eventList = new ArrayList<>();
        eventCounter = 0;
    }
}
