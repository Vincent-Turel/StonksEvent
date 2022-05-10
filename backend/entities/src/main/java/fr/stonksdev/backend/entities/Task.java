package fr.stonksdev.backend.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Kind kind;

    @NotNull
    @Embedded
    private TaskTimeBound timeBound;

    @OneToOne
    @NotNull
    private Room room;

    public Task(Room room, Kind kind, TaskTimeBound timeBound) {
        this.kind = kind;
        this.timeBound = timeBound;
        this.room = room;
    }

    public Task() {

    }

    public static Task cleaning(Room room, TaskTimeBound timeBound) {
        return new Task(room, Kind.Cleaning, timeBound);
    }

    public enum Kind {
        Cleaning
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public TaskTimeBound getTimeBound() {
        return timeBound;
    }

    public void setTimeBound(TaskTimeBound timeBound) {
        this.timeBound = timeBound;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return kind == task.kind && Objects.equals(timeBound, task.timeBound) && Objects.equals(room, task.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, timeBound, room);
    }
}
