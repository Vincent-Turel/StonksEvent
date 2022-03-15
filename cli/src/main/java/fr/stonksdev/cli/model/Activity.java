package fr.stonksdev.cli.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Activity {
    public LocalDateTime beginning;
    public Duration duration;
    public String name;
    public int maxPeopleAmount;
    public UUID id;

    public Activity(LocalDateTime beginning, Duration duration, String name, int maxPeopleAmount) {
        this.beginning = beginning;
        this.duration = duration;
        this.name = name;
        this.maxPeopleAmount = maxPeopleAmount;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", maxPeopleAmount=" + maxPeopleAmount +
                ", beginning=" + beginning +
                ", duration=" + duration +
                '}';
    }
}
