package fr.stonksdev.backend.controllers;

import fr.stonksdev.backend.controllers.dto.ErrorDTO;
import fr.stonksdev.backend.controllers.dto.StonksEventDTO;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.*;
import fr.stonksdev.backend.interfaces.EventModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
//@RequestMapping(path = EventController.EVENT_URI, produces = APPLICATION_JSON_VALUE)
// referencing the same BASE_URI as Customer care to extend it hierarchically
public class EventController {
    public static final String EVENT_URI = "/events";

    @Autowired
    private EventModifier event;

    @ExceptionHandler({ImpossibleCreationException.class})
    public ResponseEntity<ErrorDTO> handleExceptions(ImpossibleCreationException e)  {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Object creation not possible");
        errorDTO.setDetails(e.getId() + " is not a valid object Id");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler({EventIdNotFoundException.class})
    public ResponseEntity<ErrorDTO> handleExceptions(EventIdNotFoundException e)  {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("The event does not exist");
        errorDTO.setDetails(e.getName() + " is not a existing Id for an event");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler({AlreadyExistingEventException.class})
    public ResponseEntity<ErrorDTO> handleExceptions(AlreadyExistingEventException e)  {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("The event already exist");
        errorDTO.setDetails(e.getName() + " is already existing !");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler({WrongInputException.class})
    public ResponseEntity<ErrorDTO> handleExceptions(WrongInputException e)  {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Wrong parameter");
        errorDTO.setDetails("The parameter " + e.getName() + "");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDTO);
    }

    @ExceptionHandler({ItemNotFoundException.class})
    public ResponseEntity<ErrorDTO> handleExceptions(ItemNotFoundException e)  {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Wrong parameter");
        errorDTO.setDetails("The parameter " + e.getName() + "");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDTO);
    }

    @PostMapping(path = "register", consumes = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<StonksEventDTO> register(@RequestBody StonksEventDTO eventDTO)  {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertEventToDto(event.create(eventDTO.getName(), eventDTO.getAmountOfPeople(), eventDTO.getStart(), eventDTO.getDuration())));
        } catch (AlreadyExistingEventException e) {
            // Note: Returning 409 (Conflict) can also be seen a security/privacy vulnerability, exposing a service for account enumeration
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping(EVENT_URI)
    public ResponseEntity<Set<StonksEvent>> getAllEvents() {
        return ResponseEntity.ok(event.getAllEvents());
    }

    @GetMapping(EVENT_URI + "/activity")
    public ResponseEntity<Set<Activity>> getActivitiesFromEvent(@PathVariable("eventId") String eventId) throws EventIdNotFoundException {
        return ResponseEntity.ok(event.getActivitiesWithEvent(eventId));
    }

    @PostMapping(path = EVENT_URI, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateEvent(@PathVariable("eventId") String eventId, @RequestBody Activity act) throws EventIdNotFoundException, ActivityNotFoundException {
        boolean res = event.modify(eventId, act);
        return ResponseEntity.ok(res);
    }

    private StonksEventDTO convertEventToDto (StonksEvent event) { // In more complex cases, we could use ModelMapper
        return new StonksEventDTO(event.getId(), event.getName(), event.getAmountOfPeople(), event.getStart(), event.getEndDate());
    }
}
