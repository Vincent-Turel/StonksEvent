package fr.stonksdev.backend.components;

import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.components.interfaces.RoomExplorer;
import fr.stonksdev.backend.components.repositories.PlanningRepository;
import fr.stonksdev.backend.components.repositories.RoomRepository;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class RoomPlanning implements RoomExplorer {

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    PlanningRepository planningRepo;

    @Override
    public Room searchFreeRoom(RoomKind roomKind, LocalDateTime beginning, Duration duration, int minCapacity) throws RoomNotFoundException {
        return finder(Optional.of(roomKind), beginning, duration, minCapacity);
    }

    @Override
    public Room searchFreeRoom(LocalDateTime beginning, Duration duration, int minCapacity) throws RoomNotFoundException {
        return finder(Optional.empty(),beginning, duration, minCapacity);
    }

    private boolean checkRoomKind(Room item, Optional<RoomKind> roomKind) {
        if (roomKind.isEmpty()) {
            return true;
        }
        return item.getRoomKind().equals(roomKind.get());
    }

    private Room finder(Optional<RoomKind> roomKind, LocalDateTime beginning, Duration duration, int minCapacity) throws RoomNotFoundException {
        Room found = null;
        int size = -1;
        for (Room room : roomRepo.findAll()) {
            if (checkRoomKind(room, roomKind) && room.getCapacity() >= minCapacity && (size == -1 || size > room.getCapacity())) {
                Set<Activity> activities = room.getActivities();
                if (activities.isEmpty()) {
                    size = room.getCapacity();
                    found = room;
                } else {
                    boolean isFree = true;
                    for (Activity activity : activities) {
                        if (activity.getEndDate().isAfter(beginning)) {
                            isFree = false;
                        }
                    }
                    if (isFree) {
                        size = room.getCapacity();
                        found = room;
                    }
                }
            }
        }
        if (found != null) {
            return found;
        }
        throw new RoomNotFoundException();
    }
}
