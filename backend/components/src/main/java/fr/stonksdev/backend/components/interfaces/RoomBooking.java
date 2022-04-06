package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.exceptions.RoomAlreadyBookedException;
import fr.stonksdev.backend.components.exceptions.RoomIdNotFoundException;
import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.exceptions.RoomAlreadyBookedException;
import fr.stonksdev.backend.components.exceptions.RoomIdNotFoundException;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Room;

import java.util.UUID;

public interface RoomBooking {
    boolean bookRoom(Room room, Activity activity) throws ActivityNotFoundException, RoomAlreadyBookedException;

    boolean freeRoom(Room room, Activity activity) throws RoomIdNotFoundException;
}
