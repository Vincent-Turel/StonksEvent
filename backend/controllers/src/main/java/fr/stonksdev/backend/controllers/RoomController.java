package fr.stonksdev.backend.controllers;

import fr.stonksdev.backend.components.RoomManager;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.entities.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
class RoomController {
    public static final String EVENT_URI = "/planning";

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private Finder finder;

    @GetMapping(EVENT_URI + "/{eventName}/{roomName}")
    public ResponseEntity<List<TimeSlot>> planningForRoom(
            @PathVariable("eventName") String eventName,
            @PathVariable("roomName") String roomName) throws RoomNotFoundException, EventNotFoundException {
        StonksEvent event = finder.retrieveEvent(eventName);
        Room room = finder.retrieveRoom(roomName);

        List<TimeSlot> output = roomManager.getPlanningOf(event, room);
        return ResponseEntity.ok(output);
    }

    @GetMapping(EVENT_URI + "/{eventName}")
    public ResponseEntity<Map<String, List<TimeSlot>>> planningForAllRooms(
            @PathVariable("eventName") String eventName
    ) throws RoomNotFoundException, EventNotFoundException {
        StonksEvent event = finder.retrieveEvent(eventName);

        Map<String, List<TimeSlot>> output = roomManager.getPlanningOf(event);
        return ResponseEntity.ok(output);
    }
}