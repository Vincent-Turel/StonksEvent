package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.entities.*;

public interface RoomModifier {
    Room create(String name, RoomKind roomKind, int capacity) throws AlreadyExistingRoomException;

    Room modify(Long roomId, Room newRoom) throws AlreadyExistingRoomException, RoomNotFoundException;

    void delete(Room room) throws RoomNotFoundException;

    Planning getPlanningOf(StonksEvent event, Room room) throws EventNotFoundException, RoomNotFoundException;

    Planning getPlanningOf(StonksEvent event) throws EventNotFoundException, RoomNotFoundException, ActivityNotFoundException;
}
