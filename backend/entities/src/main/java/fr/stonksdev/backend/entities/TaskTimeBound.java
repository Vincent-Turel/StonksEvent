package fr.stonksdev.backend.entities;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Embeddable
public class TaskTimeBound {
    // Invariant: at least one of `before` and `after` must be non-empty.
    // This is guaranteed by the `before`, `after` and `between` methods.

    private LocalDateTime before;

    private LocalDateTime after;

    private TaskTimeBound(Optional<LocalDateTime> before, Optional<LocalDateTime> after) {
        this.before = before.orElse(null);
        this.after = after.orElse(null);
    }

    public TaskTimeBound() {

    }

    public static TaskTimeBound before(LocalDateTime before) {
        return new TaskTimeBound(Optional.of(before), Optional.empty());
    }

    public static TaskTimeBound after(LocalDateTime after) {
        return new TaskTimeBound(Optional.empty(), Optional.of(after));
    }

    public static TaskTimeBound between(LocalDateTime before, LocalDateTime after) {
        return new TaskTimeBound(Optional.of(before), Optional.of(after));
    }

    public Optional<LocalDateTime> Before() {
        return Optional.ofNullable(before);
    }

    public Optional<LocalDateTime> After() {
        return Optional.ofNullable(after);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskTimeBound that = (TaskTimeBound) o;

        if (!Objects.equals(before, that.before)) return false;
        return Objects.equals(after, that.after);
    }

    @Override
    public int hashCode() {
        int result = before != null ? before.hashCode() : 0;
        result = 31 * result + (after != null ? after.hashCode() : 0);
        return result;
    }
}
