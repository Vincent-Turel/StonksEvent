package fr.stonksdev.backend.controllers.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class StonksEventDTO {
    public String name;
    public int amountOfPeople;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public UUID id;

    private StonksEventDTO() {
    }

    public StonksEventDTO(String name, int amountOfPeople, LocalDateTime start, LocalDateTime end) {
        this.name = name;
        this.amountOfPeople = amountOfPeople;
        this.startDate = start;
        this.endDate = end;
    }

    @Override
    public String toString() {
        return "StonksEvent{" +
                ", name='" + name + '\'' +
                ", amountOfPeople=" + amountOfPeople +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
