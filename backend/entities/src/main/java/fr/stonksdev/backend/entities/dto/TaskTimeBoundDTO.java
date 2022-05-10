package fr.stonksdev.backend.entities.dto;

import fr.stonksdev.backend.entities.TaskTimeBound;

import java.time.LocalDateTime;

public class TaskTimeBoundDTO {

    public LocalDateTime before;

    public LocalDateTime after;

    public TaskTimeBoundDTO(TaskTimeBound timeBound) {
        this.before = timeBound.Before().orElse(null);
        this.after = timeBound.After().orElse(null);
    }
}
