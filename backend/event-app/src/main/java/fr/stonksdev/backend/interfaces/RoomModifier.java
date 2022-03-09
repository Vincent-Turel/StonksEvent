package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.RoomKind;
import fr.stonksdev.backend.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.exceptions.RoomIdNotFoundException;

import java.util.UUID;

public interface RoomModifier {
    Room create(String name, RoomKind roomKind, int capacity) throws AlreadyExistingRoomException;

    boolean modify(UUID roomId, Room newRoom) throws AlreadyExistingRoomException, RoomIdNotFoundException;

    boolean delete(UUID roomId) throws RoomIdNotFoundException;
}
