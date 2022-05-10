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
import java.util.stream.Collectors;

@Component
public class RoomPlanning implements RoomExplorer {

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    PlanningRepository planningRepo;

    @Override
    public Room searchFreeRoom(RoomKind roomKind, Activity activity) throws RoomNotFoundException {
        return finder(Optional.of(roomKind), activity);
    }

    @Override
    public Room searchFreeRoom(Activity activity) throws RoomNotFoundException {
        return finder(Optional.empty(), activity);
    }

    private boolean checkRoomKind(Room item, Optional<RoomKind> roomKind) {
        if (roomKind.isEmpty()) {
            return true;
        }
        return item.getRoomKind().equals(roomKind.get());
    }

    private Room finder(Optional<RoomKind> roomKind, Activity activity) throws RoomNotFoundException {
        Room found = null;
        int size = -1;
        for (Room room : roomRepo.findAll()) {
            if (checkRoomKind(room, roomKind) && room.getCapacity() >= activity.getMaxPeopleAmount() && (size == -1 || size > room.getCapacity())) {
                Set<Activity> activities = room.getActivities().stream().filter(act -> !act.equals(activity)).collect(Collectors.toSet());
                if (activities.isEmpty()) {
                    size = room.getCapacity();
                    found = room;
                } else {
                    boolean isFree = true;
                    for (Activity act : activities) {
                        if ((activity.getBeginning().isBefore(act.getEndDate()) && !activity.getBeginning().isBefore(act.getBeginning())) || (activity.getEndDate().isAfter(act.getBeginning()) && !activity.getEndDate().isAfter(act.getEndDate()))) {
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
