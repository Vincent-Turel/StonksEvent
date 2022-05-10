package fr.stonksdev.backend.entities.dto;

import fr.stonksdev.backend.entities.Task;
import fr.stonksdev.backend.entities.TaskTimeBound;

public class TaskDTO {
    public Long id;

    public Task.Kind kind;

    public TaskTimeBoundDTO timeBound;

    public Long roomId;

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.kind = task.getKind();
        this.timeBound = new TaskTimeBoundDTO(task.getTimeBound());
        this.roomId = task.getRoom().getId();
    }
}
