package fr.stonksdev.backend.entities.dto;

import java.time.LocalDateTime;

public class StonksEventDTO {
    public String name;
    public int amountOfPeople;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public Long id;

    public StonksEventDTO(String name, int amountOfPeople, LocalDateTime start, LocalDateTime end) {
        this.name = name;
        this.amountOfPeople = amountOfPeople;
        this.startDate = start;
        this.endDate = end;
    }

    private StonksEventDTO() {

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
