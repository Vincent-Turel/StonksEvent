package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.entities.TimeSlot;

import java.util.List;
import java.util.Map;

public interface RoomModifier {
    Room create(String name, RoomKind roomKind, int capacity) throws AlreadyExistingRoomException;

    Room modify(Long roomId, Room newRoom) throws AlreadyExistingRoomException, RoomNotFoundException;

    void delete(Room room) throws RoomNotFoundException;

    List<TimeSlot> getPlanningOf(StonksEvent event, Room room) throws EventNotFoundException, RoomNotFoundException;

    Map<String, List<TimeSlot>> getPlanningOf(StonksEvent event) throws EventNotFoundException, RoomNotFoundException, ActivityNotFoundException;
}
