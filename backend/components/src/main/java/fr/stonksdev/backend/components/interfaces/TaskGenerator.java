package fr.stonksdev.backend.components.interfaces;

import fr.stonksdev.backend.components.exceptions.ActivityNotFoundException;
import fr.stonksdev.backend.components.exceptions.RoomNotFoundException;
import fr.stonksdev.backend.entities.StonksEvent;
import fr.stonksdev.backend.entities.Task;

import java.util.List;

public interface TaskGenerator {
    List<Task> tasksForEvent(StonksEvent event) throws RoomNotFoundException, ActivityNotFoundException;
}
