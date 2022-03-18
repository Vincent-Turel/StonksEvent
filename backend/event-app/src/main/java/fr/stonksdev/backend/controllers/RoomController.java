package fr.stonksdev.backend.controllers;

import fr.stonksdev.backend.components.RoomManager;
import fr.stonksdev.backend.entities.TimeSlot;
import fr.stonksdev.backend.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.exceptions.EventIdNotFoundException;
import fr.stonksdev.backend.exceptions.RoomIdNotFoundException;
import fr.stonksdev.backend.exceptions.RoomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
class RoomController {
    public static final String EVENT_URI = "/planning";

    @Autowired
    private RoomManager room;

    // FIXME(scrabsha): we should *not* return a String here.
    @GetMapping(EVENT_URI + "/{eventName}/{roomName}")
    public ResponseEntity<List<TimeSlot>> planningForRoom(
            @PathVariable("eventName") String eventName,
            @PathVariable("roomName") String roomName
    ) throws EventIdNotFoundException, RoomIdNotFoundException, RoomNotFoundException {
        List<TimeSlot> output = room.getPlanningOf(eventName, roomName);
        return ResponseEntity.ok(output);
    }

    @GetMapping(EVENT_URI + "/{eventName}")
    public ResponseEntity<Map<String, List<TimeSlot>>> planningForAllRooms(
            @PathVariable("eventName") String eventName
    ) throws EventIdNotFoundException, RoomIdNotFoundException, ActivityNotFoundException, RoomNotFoundException {
        Map<String, List<TimeSlot>> output = room.getPlanningOf(eventName);
        return ResponseEntity.ok(output);
    }
}