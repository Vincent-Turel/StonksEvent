package fr.stonksdev.backend.controllers;

import fr.stonksdev.backend.components.StonksEventManager;
import fr.stonksdev.backend.components.TaskManager;
import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.exceptions.EventNotFoundException;
import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {
    public static final String EVENT_URI = "tasks";

    @Autowired
    private StonksEventManager stonksEventManager;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private Finder finder;

    @GetMapping(EVENT_URI + "{eventName}")
    public ResponseEntity<List<Task>> getTasksForEvent(@PathVariable("eventName") String eventName) throws RoomNotFoundException, ActivityNotFoundException, EventNotFoundException {
        StonksEvent event = finder.retrieveEvent(eventName);

        List<Task> taskList = taskManager.tasksForEvent(event);
        return ResponseEntity.ok(taskList);
    }
}
