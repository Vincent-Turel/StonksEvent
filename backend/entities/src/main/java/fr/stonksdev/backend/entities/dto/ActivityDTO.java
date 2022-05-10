package fr.stonksdev.backend.entities.dto;

import fr.stonksdev.backend.entities.Duration;

import java.time.LocalDateTime;

public class ActivityDTO {
    public String name;
    public int maxPeopleAmount;
    public LocalDateTime beginning;
    public Duration duration;
    public Long id;

    public ActivityDTO(String name, int maxPeopleAmount, LocalDateTime beginning, Duration duration) {
        this.name = name;
        this.maxPeopleAmount = maxPeopleAmount;
        this.beginning = beginning;
        this.duration = duration;
    }

    private ActivityDTO() {
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
