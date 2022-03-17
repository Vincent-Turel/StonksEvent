package fr.stonksdev.backend.controllers.dto;

import fr.stonksdev.backend.entities.Duration;

import java.time.LocalDateTime;
import java.util.UUID;

public class ActivtyDTO {
    private String name;
    private int maxPeopleAmount;
    private String description;
    private LocalDateTime beginning;
    private Duration duration;
    private UUID id;

    public ActivtyDTO(String name, int maxPeopleAmount, String description, LocalDateTime beginning, Duration duration) {
        this.name = name;
        this.maxPeopleAmount = maxPeopleAmount;
        this.description = description;
        this.beginning = beginning;
        this.duration = duration;
    }

    private ActivtyDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPeopleAmount() {
        return maxPeopleAmount;
    }

    public void setMaxPeopleAmount(int maxPeopleAmount) {
        this.maxPeopleAmount = maxPeopleAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getBeginning() {
        return beginning;
    }

    public void setBeginning(LocalDateTime beginning) {
        this.beginning = beginning;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "ActivtyDTO{" +
                "name='" + name + '\'' +
                ", maxPeopleAmount=" + maxPeopleAmount +
                ", description='" + description + '\'' +
                ", beginning=" + beginning +
                ", duration=" + duration +
                '}';
    }
}
