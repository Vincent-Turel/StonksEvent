package fr.stonksdev.backend.controllers;

import fr.stonksdev.backend.components.InMemoryDatabase;
import fr.stonksdev.backend.components.RoomManager;
import fr.stonksdev.backend.components.StonksEventManager;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.entities.TimeSlot;
import fr.stonksdev.backend.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.exceptions.EventIdNotFoundException;
import fr.stonksdev.backend.exceptions.RoomIdNotFoundException;
import fr.stonksdev.backend.exceptions.RoomNotFoundException;
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
    private StonksEventManager stonksEventManager;

    @Autowired
    private InMemoryDatabase inMemoryDatabase;

    @GetMapping(EVENT_URI + "/{eventName}/{roomName}")
    public ResponseEntity<List<TimeSlot>> planningForRoom(
            @PathVariable("eventName") String eventName,
            @PathVariable("roomName") String roomName
    ) throws EventIdNotFoundException, RoomIdNotFoundException, RoomNotFoundException {
        StonksEvent event = stonksEventManager.findByName(eventName);
        Room room = roomManager.findByName(roomName);

        List<TimeSlot> output = roomManager.getPlanningOf(event, room);
        return ResponseEntity.ok(output);
    }

    @GetMapping(EVENT_URI + "/{eventName}")
    public ResponseEntity<Map<String, List<TimeSlot>>> planningForAllRooms(
            @PathVariable("eventName") String eventName
    ) throws EventIdNotFoundException, RoomIdNotFoundException, ActivityNotFoundException, RoomNotFoundException {
        StonksEvent event = stonksEventManager.findByName(eventName);

        Map<String, List<TimeSlot>> output = roomManager.getPlanningOf(event);
        return ResponseEntity.ok(output);
    }
}