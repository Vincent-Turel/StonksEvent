package fr.stonksdev.cli.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class StonksEvent {
    public String name;
    public int amountOfPeople;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public Long id;

    public StonksEvent(String name, int amountOfPeople, LocalDateTime startDate, LocalDateTime endDate)  {
        this.name = name;
        this.amountOfPeople = amountOfPeople;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private StonksEvent() {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StonksEvent)) return false;
        StonksEvent that = (StonksEvent) o;
        return amountOfPeople == that.amountOfPeople && name.equals(that.name) && startDate.isEqual(that.startDate)
                && endDate.isEqual(that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, amountOfPeople, startDate, endDate, id);
    }
}
