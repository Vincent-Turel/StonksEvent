package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.entities.TimeSlot;
import fr.stonksdev.backend.exceptions.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface RoomModifier {
    boolean create(String name, RoomKind roomKind, int capacity) throws AlreadyExistingRoomException;

    boolean modify(UUID roomId, Room newRoom) throws AlreadyExistingRoomException, RoomIdNotFoundException;

    boolean delete(UUID roomId) throws RoomIdNotFoundException;

    List<TimeSlot> getPlanningOf(String eventName, String roomName) throws EventIdNotFoundException, RoomIdNotFoundException, RoomNotFoundException;

    Map<String, List<TimeSlot>> getPlanningOf(String eventName) throws EventIdNotFoundException, RoomIdNotFoundException, ActivityNotFoundException, RoomNotFoundException;
}
