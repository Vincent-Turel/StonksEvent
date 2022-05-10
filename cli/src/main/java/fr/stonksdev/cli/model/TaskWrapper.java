package fr.stonksdev.cli.model;

import java.util.List;

public class TaskWrapper {
    public List<Task> tasks;

    public TaskWrapper(List<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskWrapper() {

    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("All tasks :\n");
        for (Task element: tasks ) {
            res.append(element.toString()).append("\n");
        }
        res.append("}");
        return res.toString();
    }
}
