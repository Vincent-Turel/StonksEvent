package fr.stonksdev.cli.model;

public class Task {

    public Long id;

    public Kind kind;

    public TaskTimeBound timeBound;

    public Long roomId;

    public Task() {

    }

    @Override
    public String toString() {
        return "Task{" +
                "id = " + id +
                ", kind = " + kind +
                ", timeBound = " + timeBound +
                ", room id = " + roomId +
                '}';
    }
}
