package fr.stonksdev.backend.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class StonksEvent implements Serializable {
    private final String id;
    private final String name;
    private final int amountOfPeople;
    private final LocalDateTime start;
    private final Duration duration;

    public StonksEvent(String name, int amountOfPeople, LocalDateTime start, Duration duration) {
        this.name = name;
        this.amountOfPeople = amountOfPeople;
        this.start = start;
        this.duration = duration;
        this.id = UUID.randomUUID().toString();
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

    public LocalDateTime getStart() {
        return start;
    }

    public Duration getEndDate() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StonksEvent)) return false;
        StonksEvent that = (StonksEvent) o;
        return getAmountOfPeople() == that.getAmountOfPeople() && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAmountOfPeople());
    }
}
