package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.StonksEvent;
import org.springframework.stereotype.Component;

import java.util.*;

import static fr.stonksdev.backend.entities.RoomKind.*;

@Component
public class InMemoryDatabase {
    private int eventCounter = 0;
    private int activityCounter = 0;

    private Map<UUID, StonksEvent> events;

    public Map<UUID, StonksEvent> getEvents() {
        return events;
    }

    public UUID addEvent(StonksEvent newEvent) {
        events.put(newEvent.getId(), newEvent);
        eventCounter++;
        return newEvent.getId();
    }

    private Map<String, Activity> activities;

    public Map<String, Activity> getActivities() {
        return activities;
    }

    public boolean addActivity(Activity newActivity) {
        int check = 0;
        if (!activities.isEmpty()) {
            for (Map.Entry<String, Activity> item : activities.entrySet()) {
                if (item.getKey().equals(newActivity.getEventUUID().toString() + newActivity.getName())) {
                    check++;
                }
            }
        }
        if (check == 0) {
            activities.put(newActivity.getEventUUID().toString() + newActivity.getName(), newActivity);
            activityCounter++;
            return true;
        }
        return false;
    }

    public List<String> deleteEvent(UUID eventId) {
        List<String> activitiesDel = new ArrayList<>();
        if (!activities.isEmpty()) {
            Iterator<Map.Entry<String, Activity>> it = activities.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Activity> item = it.next();
                if (item.getKey().contains(eventId.toString())) {
                    activitiesDel.add(item.getValue().getName());
                    it.remove();
                    activityCounter--;
                }
            }
        }
        events.remove(eventId);
        eventCounter--;
        return activitiesDel;
    }

    public void deleteActivity(String activityId) {
        activities.remove(activityId);
        activityCounter--;
    }


    private Map<Room, Set<Activity>> rooms;

    public Map<Room, Set<Activity>> getRooms() {
        return rooms;
    }

    public InMemoryDatabase() {
        events = new HashMap<>();
        activities = new HashMap<>();
        rooms = new HashMap<>();
        eventCounter = 0;
    }

    public int getEventCounter() {
        return eventCounter;
    }

    public int getActivityCounter() {
        return activityCounter;
    }

    public void flush() {
        events.clear();
        activities.clear();
        rooms.clear();
        eventCounter = 0;
    }
}
