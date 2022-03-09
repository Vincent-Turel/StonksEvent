package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Duration;
import fr.stonksdev.backend.exceptions.RoomAlreadyBookedException;
import fr.stonksdev.backend.exceptions.RoomIdNotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

public interface RoomBooking {
    boolean bookRoom(UUID roomId, LocalDateTime beginning, Duration duration, String activityId) throws RoomIdNotFoundException, RoomAlreadyBookedException;

    boolean freeRoom(UUID roomId, String activityId);
}
