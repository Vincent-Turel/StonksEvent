package fr.stonksdev.backend.controllers;

import fr.stonksdev.backend.components.StonksEventManager;
import fr.stonksdev.backend.components.exceptions.AlreadyExistingEventException;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.components.interfaces.StonksEventFinder;
import fr.stonksdev.backend.entities.dto.ErrorDTO;
import fr.stonksdev.backend.entities.dto.StonksEventDTO;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.StonksEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
//@RequestMapping(path = EventController.EVENT_URI, produces = APPLICATION_JSON_VALUE)
// referencing the same BASE_URI as Customer care to extend it hierarchically
public class StonksEventController {
    public static final String EVENTS_URI = "/events";
    public static final String EVENT_URI = EVENTS_URI + "/{eventId}";
    public static final String ACTIVITY_URI = EVENT_URI + "/activities";


    @Autowired
    private StonksEventManager eventManager;

    @Autowired
    private Finder finder;

    @ExceptionHandler({EventNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(EventNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("The event does not exist");
        errorDTO.setDetails(e.getName() + " is not a existing Id for an event");
        return errorDTO;
    }

    @ExceptionHandler({AlreadyExistingEventException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleExceptions(AlreadyExistingEventException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("The event already exist");
        errorDTO.setDetails(e.getName() + " is already existing !");
        return errorDTO;
    }

    @PostMapping(path = EVENTS_URI, consumes = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<StonksEventDTO> registerEvent(@RequestBody @Valid StonksEventDTO eventDTO) throws AlreadyExistingEventException {
        StonksEventDTO test = convertEventToDto(eventManager.createEvent(eventDTO.name, eventDTO.amountOfPeople, eventDTO.startDate, eventDTO.endDate));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(test);
    }

    @PostMapping(path = EVENT_URI, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<StonksEventDTO> updateEvent(@PathVariable("eventId") Long eventId, @RequestBody @Valid StonksEventDTO eventDTO) throws EventNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertEventToDto(eventManager.updateEvent(eventId, eventDTO.amountOfPeople, eventDTO.startDate, eventDTO.endDate)));
    }

    private StonksEventDTO convertEventToDto(StonksEvent event) {
        StonksEventDTO eventDTO = new StonksEventDTO(event.getName(), event.getAmountOfPeople(), event.getStartDate(), event.getEndDate());
        eventDTO.id = event.getId();
        return eventDTO;
    }

    @GetMapping(ACTIVITY_URI)
    public ResponseEntity<Set<Activity>> getAllActivitiesFromEvent(@PathVariable("eventId") Long eventId) throws EventNotFoundException {
        return ResponseEntity.ok(new HashSet<>(eventManager.getAllActivitiesFromEvent(finder.retrieveEvent(eventId))));
    }
}
