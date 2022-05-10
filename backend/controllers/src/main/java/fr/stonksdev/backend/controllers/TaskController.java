package fr.stonksdev.backend.controllers;

import fr.stonksdev.backend.components.TaskManager;
import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.entities.Task;
import fr.stonksdev.backend.entities.dto.ErrorDTO;
import fr.stonksdev.backend.entities.dto.TaskWrapperDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
    public static final String TASKS_URI = "tasks/";

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private Finder finder;

    @ExceptionHandler({RoomNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(RoomNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Haven't been able to find any room.");
        errorDTO.setDetails("The research for rooms to host activity has failed because no rooms were available ");
        return errorDTO;
    }

    @GetMapping(TASKS_URI + "{eventId}")
    public ResponseEntity<TaskWrapperDTO> getTasksForEvent(@PathVariable("eventId") Long eventId) throws RoomNotFoundException, ActivityNotFoundException, EventNotFoundException {
        StonksEvent event = finder.retrieveEvent(eventId);

        List<Task> taskList = taskManager.tasksForEvent(event);
        return ResponseEntity.ok(new TaskWrapperDTO(taskList));
    }
}
