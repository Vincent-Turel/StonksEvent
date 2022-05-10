package fr.stonksdev.cli.model;

import java.time.LocalDateTime;

public class TaskTimeBound {
    // Invariant: at least one of `before` and `after` must be non-empty.
    // This is guaranteed by the `before`, `after` and `between` methods.

    public LocalDateTime before;

    public LocalDateTime after;

    public TaskTimeBound(LocalDateTime before, LocalDateTime after) {
        this.before = before;
        this.after = after;
    }

    public TaskTimeBound() {

    }

    @Override
    public String toString() {
        if (before == null) {
            return "TaskTimeBound{ after = " + after + '}';
        } else if (after == null) {
            return "TaskTimeBound{ before = " + before + '}';
        } else {
            return "TaskTimeBound{" +
                    "before = " + before +
                    ", after = " + after +
                    '}';
        }
    }
}