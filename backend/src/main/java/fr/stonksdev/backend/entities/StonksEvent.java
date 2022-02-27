package fr.stonksdev.backend.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class StonksEvent implements Serializable {

    private String id;
    private String name;
    private int amountOfPeople;
    private List<Activity> activities;
    private Date startDate;
    private Date endDate;

    public StonksEvent(String name, int amountOfPeople, Date startDate, Date endDate)  {
        this.name = name;
        this.amountOfPeople = amountOfPeople;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public List<Activity> getActivities() {
        return activities;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StonksEvent)) return false;
        StonksEvent that = (StonksEvent) o;
        return getAmountOfPeople() == that.getAmountOfPeople() && Objects.equals(getName(), that.getName()) && Objects.equals(getActivities(), that.getActivities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAmountOfPeople(), getActivities());
    }
}
