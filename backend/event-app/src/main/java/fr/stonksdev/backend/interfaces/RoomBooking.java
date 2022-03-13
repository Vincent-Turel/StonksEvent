package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.exceptions.RoomAlreadyBookedException;
import fr.stonksdev.backend.exceptions.RoomIdNotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

public interface RoomBooking {
    boolean bookRoom(UUID roomId, UUID activityId) throws ActivityNotFoundException, RoomAlreadyBookedException;

    boolean freeRoom(UUID roomId, UUID activityId) throws RoomIdNotFoundException;
}
