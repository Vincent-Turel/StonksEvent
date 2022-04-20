package fr.stonksdev.backend.entities.dto;

import fr.stonksdev.backend.entities.Duration;

import java.time.LocalDateTime;
import java.util.UUID;

public class ActivtyDTO {
    private String name;
    private int maxPeopleAmount;
    private LocalDateTime beginning;
    private Duration duration;
    private Long id;

    public ActivtyDTO(String name, int maxPeopleAmount, LocalDateTime beginning, Duration duration) {
        this.name = name;
        this.maxPeopleAmount = maxPeopleAmount;
        this.beginning = beginning;
        this.duration = duration;
    }

    private ActivtyDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
                ", beginning=" + beginning +
                ", duration=" + duration +
                '}';
    }
}
