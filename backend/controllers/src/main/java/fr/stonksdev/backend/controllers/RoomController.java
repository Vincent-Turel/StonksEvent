package fr.stonksdev.backend.controllers;

import fr.stonksdev.backend.components.RoomManager;
import fr.stonksdev.backend.components.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.components.exceptions.AlreadyExistingRoomException;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.entities.Planning;
import fr.stonksdev.backend.entities.Room;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.entities.dto.ErrorDTO;
import fr.stonksdev.backend.entities.dto.PlanningDTO;
import fr.stonksdev.backend.entities.dto.RoomDTO;
import fr.stonksdev.backend.entities.dto.StonksEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
class RoomController {
    public static final String PLANNING_URI = "/planning";
    public static final String ROOM_URI = "/room";

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private Finder finder;

    @ExceptionHandler({RoomNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(RoomNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("The room was not found.");
        errorDTO.setDetails(e.getName() + " doesn't exist !");
        return errorDTO;
    }

    @ExceptionHandler({EventNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(EventNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("The event was not found.");
        errorDTO.setDetails(e.getName() + " doesn't exist !");
        return errorDTO;
    }

    @ExceptionHandler({AlreadyExistingRoomException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleExceptions(AlreadyExistingRoomException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("The room already exists.");
        errorDTO.setDetails(e.getName() + " is already existing !");
        return errorDTO;
    }

    @PostMapping(path = ROOM_URI, consumes = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<RoomDTO> registerRoom(@RequestBody @Valid RoomDTO roomDTO) throws AlreadyExistingRoomException {
        RoomDTO createdRoom = new RoomDTO(roomManager.create(roomDTO.name, roomDTO.roomKind, roomDTO.capacity));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdRoom);
    }

    @GetMapping(PLANNING_URI + "/{eventName}/{roomName}")
    public ResponseEntity<PlanningDTO> planningForRoom(
            @PathVariable("eventName") String eventName,
            @PathVariable("roomName") String roomName) throws RoomNotFoundException, EventNotFoundException {
        StonksEvent event = finder.retrieveEvent(eventName);
        Room room = finder.retrieveRoom(roomName);

        Planning planning = roomManager.getPlanningOf(event, room);
        PlanningDTO planningOutput = convertPlanningToDto(planning);
        return ResponseEntity.ok(planningOutput);
    }

    @GetMapping(PLANNING_URI + "/{eventName}")
    public ResponseEntity<PlanningDTO> planningForAllRooms(
            @PathVariable("eventName") String eventName
    ) throws RoomNotFoundException, EventNotFoundException {
        StonksEvent event = finder.retrieveEvent(eventName);

        Planning planning = roomManager.getPlanningOf(event);
        PlanningDTO planningOutput = convertPlanningToDto(planning);
        return ResponseEntity.ok(planningOutput);
    }

    private PlanningDTO convertPlanningToDto(Planning planning) {
        PlanningDTO planningDTO = new PlanningDTO(planning.getEvent(), planning.getTimeSlots());
        planningDTO.id = planning.getId();
        return planningDTO;
    }
}