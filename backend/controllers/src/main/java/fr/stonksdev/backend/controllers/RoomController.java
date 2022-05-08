package fr.stonksdev.backend.controllers;

import fr.stonksdev.backend.components.RoomManager;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.entities.Planning;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.StonksEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class RoomController {
    public static final String EVENT_URI = "/planning";

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private Finder finder;

    @GetMapping(EVENT_URI + "/{eventName}/{roomName}")
    public ResponseEntity<Planning> planningForRoom(
            @PathVariable("eventName") String eventName,
            @PathVariable("roomName") String roomName) throws RoomNotFoundException, EventNotFoundException {
        StonksEvent event = finder.retrieveEvent(eventName);
        Room room = finder.retrieveRoom(roomName);

        Planning output = roomManager.getPlanningOf(event, room);
        return ResponseEntity.ok(output);
    }

    @GetMapping(EVENT_URI + "/{eventName}")
    public ResponseEntity<Planning> planningForAllRooms(
            @PathVariable("eventName") String eventName
    ) throws RoomNotFoundException, EventNotFoundException {
        StonksEvent event = finder.retrieveEvent(eventName);

        Planning output = roomManager.getPlanningOf(event);
        return ResponseEntity.ok(output);
    }
}