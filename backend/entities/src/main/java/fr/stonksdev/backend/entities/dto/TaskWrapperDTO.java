package fr.stonksdev.backend.entities.dto;

import fr.stonksdev.backend.entities.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskWrapperDTO {
    public List<TaskDTO> tasks;

    public TaskWrapperDTO(List<Task> list) {
        tasks = new ArrayList<>();
        for (Task task: list) {
            tasks.add(new TaskDTO(task));
        }
    }

    public TaskWrapperDTO(){

    }
}
