package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryDatabase {
    private int eventCounter = 0;
    private List<Activity> eventList;

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
