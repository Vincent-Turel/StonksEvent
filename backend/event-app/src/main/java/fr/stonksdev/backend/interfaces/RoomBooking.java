package fr.stonksdev.backend.interfaces;

import fr.stonksdev.backend.entities.Duration;

import java.time.LocalDateTime;

public interface RoomBooking {
    boolean bookRoom(String roomId, LocalDateTime beginning, Duration duration, String activityId);

    boolean freeRoom(String roomId, String activityId);
}
