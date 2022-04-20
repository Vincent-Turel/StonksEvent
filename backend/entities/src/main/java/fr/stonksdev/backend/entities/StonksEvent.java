package fr.stonksdev.backend.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name= "events")
public class StonksEvent {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String name;

    @NotNull
    private int amountOfPeople;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    private Set<Activity> activities;

    public StonksEvent(String name, int amountOfPeople, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.amountOfPeople = amountOfPeople;
        this.startDate = startDate;
        this.endDate = endDate;
        activities = new HashSet<>();
    }

    public StonksEvent() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setAmountOfPeople(int newNumber) {
        this.amountOfPeople = newNumber;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public int getAmountOfPeople() {
        return amountOfPeople;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StonksEvent)) return false;
        StonksEvent that = (StonksEvent) o;
        return getAmountOfPeople() == that.getAmountOfPeople()
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getStartDate(), that.getStartDate())
                && Objects.equals(getEndDate(), that.getEndDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAmountOfPeople(), getStartDate(), getEndDate());
    }
}
