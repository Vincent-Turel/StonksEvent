package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.components.exceptions.*;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.entities.TimeSlot;

import java.util.List;
import java.util.Map;

public interface RoomModifier {
    boolean create(String name, RoomKind roomKind, int capacity) throws AlreadyExistingRoomException;

    boolean modify(Room room, Room newRoom) throws AlreadyExistingRoomException, RoomIdNotFoundException;

    boolean delete(Room room) throws RoomIdNotFoundException;

    List<TimeSlot> getPlanningOf(StonksEvent event, Room room) throws EventIdNotFoundException, RoomIdNotFoundException, RoomNotFoundException;

    Map<String, List<TimeSlot>> getPlanningOf(StonksEvent event) throws EventIdNotFoundException, RoomIdNotFoundException, ActivityNotFoundException, RoomNotFoundException;
}
