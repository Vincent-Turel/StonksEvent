package fr.stonksdev.backend.controllers.dto;

import fr.stonksdev.backend.entities.Duration;

import java.time.LocalDateTime;

public class StonksEventDTO {
    private final String id;
    private String name;
    private int amountOfPeople;
    private LocalDateTime start;
    private Duration duration;

    public StonksEventDTO(String id, String name, int amountOfPeople, LocalDateTime start, Duration duration) {
        this.id = id;
        this.name = name;
        this.amountOfPeople = amountOfPeople;
        this.start = start;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmountOfPeople() {
        return amountOfPeople;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmountOfPeople(int amountOfPeople) {
        this.amountOfPeople = amountOfPeople;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setStartDate(LocalDateTime start) {
        this.start = start;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "StonksEvent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", amountOfPeople=" + amountOfPeople +
                '}';
    }
}
