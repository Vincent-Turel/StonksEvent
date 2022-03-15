package fr.stonksdev.cli.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class StonksEvent {
    public String name;
    public int amountOfPeople;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public UUID id;

    public StonksEvent(String name, int amountOfPeople, LocalDateTime startDate, LocalDateTime endDate)  {
        this.name = name;
        this.amountOfPeople = amountOfPeople;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "StonksEvent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", amountOfPeople=" + amountOfPeople +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
