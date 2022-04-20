package fr.stonksdev.backend.controllers;

import fr.stonksdev.backend.components.ActivityManager;
import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.exceptions.AlreadyExistingActivityException;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.dto.ActivtyDTO;
import fr.stonksdev.backend.entities.dto.ErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ActivityController {
    public static final String EVENTS_URI = "/events";
    public static final String EVENT_URI = EVENTS_URI + "/{eventId}";
    public static final String ACTIVITY_URI = EVENT_URI + "/activities";

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private Finder finder;

    @ExceptionHandler({AlreadyExistingActivityException.class})
    public ResponseEntity<ErrorDTO> handleExceptions(AlreadyExistingActivityException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("The activity already exist");
        errorDTO.setDetails(e.getName() + " is already existing !");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @PostMapping(path = ACTIVITY_URI, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivtyDTO> registerActivity(@PathVariable("eventId") Long eventId, @RequestBody ActivtyDTO activtyDTO) throws EventNotFoundException, AlreadyExistingActivityException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertActivityToDto(activityManager.createActivity(activtyDTO.getBeginning(), activtyDTO.getDuration(), activtyDTO.getName(), activtyDTO.getMaxPeopleAmount(), finder.retrieveEvent(eventId))));
    }

    @PostMapping(path = ACTIVITY_URI + "/{activityId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivtyDTO> updateActivity(@PathVariable("activityId") Long activityId, @RequestBody ActivtyDTO activtyDTO) throws ActivityNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertActivityToDto(activityManager.updateActivity(activityId, activtyDTO.getMaxPeopleAmount(), activtyDTO.getBeginning(), activtyDTO.getDuration())));
    }

    private ActivtyDTO convertActivityToDto(Activity activity) {
        ActivtyDTO activityDTO = new ActivtyDTO(activity.getName(), activity.getMaxPeopleAmount(), activity.getBeginning(), activity.getDuration());
        activityDTO.setId(activity.getId());
        return activityDTO;
    }
}
