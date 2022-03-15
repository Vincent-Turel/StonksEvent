package fr.stonksdev.backend.controllers;

import fr.stonksdev.backend.components.StonksEventManager;
import fr.stonksdev.backend.controllers.dto.ActivtyDTO;
import fr.stonksdev.backend.controllers.dto.ErrorDTO;
import fr.stonksdev.backend.controllers.dto.StonksEventDTO;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @ExceptionHandler({EventIdNotFoundException.class})
    public ResponseEntity<ErrorDTO> handleExceptions(EventIdNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("The event does not exist");
        errorDTO.setDetails(e.getName() + " is not a existing Id for an event");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler({AlreadyExistingEventException.class})
    public ResponseEntity<ErrorDTO> handleExceptions(AlreadyExistingEventException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("The event already exist");
        errorDTO.setDetails(e.getName() + " is already existing !");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @PostMapping(path = EVENTS_URI, consumes = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<StonksEventDTO> registerEvent(@RequestBody StonksEventDTO eventDTO) throws AlreadyExistingEventException {
        StonksEventDTO test = convertEventToDto(eventManager.createEvent(eventDTO.name, eventDTO.amountOfPeople, eventDTO.startDate, eventDTO.endDate));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(test);
    }

    @PostMapping(path = EVENT_URI, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<StonksEventDTO> updateEvent(@PathVariable("eventId") UUID eventId, @RequestBody StonksEventDTO eventDTO) throws EventIdNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertEventToDto(eventManager.updateEvent(eventId, eventDTO.amountOfPeople, eventDTO.startDate, eventDTO.endDate)));
    }

    @PostMapping(path = ACTIVITY_URI, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivtyDTO> registerActivity(@PathVariable("eventId") UUID eventId, @RequestBody ActivtyDTO activtyDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertActivityToDto(eventManager.createActivity(activtyDTO.getBeginning(), activtyDTO.getDuration(), activtyDTO.getName(), eventId)));
    }

    @PostMapping(path = ACTIVITY_URI + "/{activityId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivtyDTO> updateActivity(@PathVariable("activityId") UUID activityId, @RequestBody ActivtyDTO activtyDTO) throws ActivityNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertActivityToDto(eventManager.updateActivity(activityId, activtyDTO.getMaxPeopleAmount(), activtyDTO.getBeginning(), activtyDTO.getDuration())));
    }

    @GetMapping(ACTIVITY_URI)
    public ResponseEntity<Set<Activity>> getAllActivitiesFromEvent(@PathVariable("eventId") UUID eventId) {
        return ResponseEntity.ok(new HashSet<>(eventManager.getAllActivitiesFromEvent(eventId)));
    }

    private StonksEventDTO convertEventToDto(StonksEvent event) {
        StonksEventDTO eventDTO = new StonksEventDTO(event.getName(), event.getAmountOfPeople(), event.getStartDate(), event.getEndDate());
        eventDTO.id = event.getId();
        return eventDTO;
    }

    private ActivtyDTO convertActivityToDto(Activity activity) {
        ActivtyDTO activityDTO = new ActivtyDTO(activity.getName(), activity.getMaxPeopleAmount(), activity.getDescription(), activity.getBeginning(), activity.getDuration());
        activityDTO.setId(activity.getActivityID());
        return activityDTO;
    }
}
