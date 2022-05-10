package fr.stonksdev.backend.controllers;

import fr.stonksdev.backend.components.ActivityManager;
import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.exceptions.AlreadyExistingActivityException;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.entities.Activity;
import fr.stonksdev.backend.entities.dto.ActivityDTO;
import fr.stonksdev.backend.entities.dto.ActivityWrapperDTO;
import fr.stonksdev.backend.entities.dto.ErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ActivityController {
    public static final String EVENTS_URI = "/events";
    public static final String EVENT_URI = EVENTS_URI + "/{eventId}";
    public static final String ACTIVITIES_URI = "/activities";
    public static final String ACTIVITY_URI = EVENT_URI + ACTIVITIES_URI;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private Finder finder;

    @ExceptionHandler({ActivityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(EventNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("The activity does not exist");
        errorDTO.setDetails(e.getName() + " is not a existing Id for an activity");
        return errorDTO;
    }

    @ExceptionHandler({AlreadyExistingActivityException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleExceptions(AlreadyExistingActivityException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("The activity already exist");
        errorDTO.setDetails(e.getName() + " is already existing !");
        return errorDTO;
    }

    @PostMapping(path = ACTIVITY_URI, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivityDTO> registerActivity(@PathVariable("eventId") Long eventId, @RequestBody @Valid ActivityDTO activityDTO) throws EventNotFoundException, AlreadyExistingActivityException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertActivityToDto(activityManager.createActivity(activityDTO.beginning, activityDTO.duration, activityDTO.name, activityDTO.maxPeopleAmount, finder.retrieveEvent(eventId))));
    }

    @PostMapping(path = ACTIVITY_URI + "/{activityId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivityDTO> updateActivity(@PathVariable("activityId") Long activityId, @RequestBody @Valid ActivityDTO activityDTO) throws ActivityNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertActivityToDto(activityManager.updateActivity(activityId, activityDTO.maxPeopleAmount, activityDTO.beginning, activityDTO.duration)));
    }

    @GetMapping(ACTIVITIES_URI)
    public ResponseEntity<ActivityWrapperDTO> getAllActivities() {
        return ResponseEntity.ok(
                new ActivityWrapperDTO(
                        convertListActivityToDto(
                                activityManager.getAllActivities()
                        )
                )
        );
    }

    private ActivityDTO convertActivityToDto(Activity activity) {
        ActivityDTO activityDTO = new ActivityDTO(activity.getName(), activity.getMaxPeopleAmount(), activity.getBeginning(), activity.getDuration());
        activityDTO.id = activity.getId();
        return activityDTO;
    }

    private List<ActivityDTO> convertListActivityToDto(List<Activity> list) {
        List<ActivityDTO> activityDTOList = new ArrayList<>();
        for (Activity activity : list) {
            activityDTOList.add(convertActivityToDto(activity));
        }
        return activityDTOList;
    }
}
