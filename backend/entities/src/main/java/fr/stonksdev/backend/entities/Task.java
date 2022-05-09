package fr.stonksdev.backend.entities;

import java.util.Objects;

public class Task {
    public Kind kind;
    public TaskTimeBound timeBound;

    public Task(Kind kind, TaskTimeBound timeBound) {
        this.kind = kind;
        this.timeBound = timeBound;
    }

    public static Task cleaning(String room, TaskTimeBound timeBound) {
        return new Task(Kind.Cleaning, timeBound);
    }

    public enum Kind {
        Cleaning
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (kind != task.kind) return false;
        return Objects.equals(timeBound, task.timeBound);
    }

    @Override
    public int hashCode() {
        int result = kind != null ? kind.hashCode() : 0;
        result = 31 * result + (timeBound != null ? timeBound.hashCode() : 0);
        return result;
    }
}
