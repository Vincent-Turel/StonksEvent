package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Duration;

import java.time.LocalDateTime;
import java.util.UUID;

public interface RoomBooking {
    boolean bookRoom(UUID roomId, LocalDateTime beginning, Duration duration, String activityId);

    boolean freeRoom(UUID roomId, String activityId);
}
