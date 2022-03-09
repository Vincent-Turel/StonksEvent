package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.exceptions.RoomIdNotFoundException;

public interface RoomModifier {
    Room create(String name, RoomKind roomKind, int capacity) throws AlreadyExistingRoomException;

    boolean modify(String roomId, Room newRoom) throws AlreadyExistingRoomException, RoomIdNotFoundException;

    boolean delete(String roomId);
}
