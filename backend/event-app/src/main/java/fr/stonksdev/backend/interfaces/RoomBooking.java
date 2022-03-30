package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.exceptions.RoomAlreadyBookedException;
import fr.stonksdev.backend.exceptions.RoomIdNotFoundException;

public interface RoomBooking {
    boolean bookRoom(Room room, Activity activity) throws ActivityNotFoundException, RoomAlreadyBookedException;

    boolean freeRoom(Room room, Activity activity) throws RoomIdNotFoundException;
}
