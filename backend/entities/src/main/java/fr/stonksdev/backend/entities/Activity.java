package fr.stonksdev.backend.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Activity {
    private LocalDateTime beginning;
    private Duration duration;
    private String name;
    private String description;
    private int maxPeopleAmount;
    private final UUID eventUUID;

    public Activity(LocalDateTime beginning, Duration duration, String name, String description, int maxPeopleAmount, UUID eventUUID) {
        this.beginning = beginning;
        this.duration = duration;
        this.name = name;
        this.description = description;
        this.maxPeopleAmount = maxPeopleAmount;
        this.eventUUID = eventUUID;
    }

    public Activity(LocalDateTime beginning, Duration duration, String name, int maxPeopleAmount, UUID eventUUID) {
        this.beginning = beginning;
        this.duration = duration;
        this.name = name;
        // FIXME(scrabsha): is this a good idea to provide a placeholder text
        // here?
        this.description = "Empty";
        this.maxPeopleAmount = maxPeopleAmount;
        this.eventUUID = eventUUID;
    }

    public Activity(LocalDateTime beginning, Duration duration, String name, UUID eventUUID) {
        this.beginning = beginning;
        this.duration = duration;
        this.name = name;
        this.description = "Empty";
        this.maxPeopleAmount = 9999;
        this.eventUUID = eventUUID;
    }

    public LocalDateTime getBeginning() {
        return beginning;
    }

    public void setBeginning(LocalDateTime newBeginning) {
        this.beginning = newBeginning;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration newDuration) {
        this.duration = newDuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public int getMaxPeopleAmount() {
        return maxPeopleAmount;
    }

    public void setMaxPeopleAmount(int newMaxPeopleAmount) {
        this.maxPeopleAmount = newMaxPeopleAmount;
    }

    public UUID getEventUUID() {
        return this.eventUUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        Activity that = (Activity) o;
        return getMaxPeopleAmount() == that.getMaxPeopleAmount()
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getEventUUID(), that.getEventUUID())
                && Objects.equals(getDescription(), that.getDescription())
                && Objects.equals(getDuration(), that.getDuration())
                && Objects.equals(getBeginning(), that.getBeginning());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getBeginning(), getEventUUID(), getDescription(), getDuration(), getMaxPeopleAmount());
    }
}
