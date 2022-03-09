package fr.stonksdev.backend.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class StonksEvent implements Serializable {
    private String name;
    private int amountOfPeople;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private final UUID id;

    public StonksEvent(String name, int amountOfPeople, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.amountOfPeople = amountOfPeople;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public int getAmountOfPeople() {
        return amountOfPeople;
    }

    public void setAmountOfPeople(int newNumber) {
        this.amountOfPeople = newNumber;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StonksEvent)) return false;
        StonksEvent that = (StonksEvent) o;
        return getAmountOfPeople() == that.getAmountOfPeople()
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getId(), that.getId())
                && Objects.equals(getStartDate(), that.getStartDate())
                && Objects.equals(getEndDate(), that.getEndDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAmountOfPeople(), getId(), getStartDate(), getEndDate());
    }
}
