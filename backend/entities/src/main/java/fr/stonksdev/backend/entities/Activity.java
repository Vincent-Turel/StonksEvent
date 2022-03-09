package fr.stonksdev.backend.entities;

import java.time.LocalDateTime;

public class Activity {
    private LocalDateTime beginning;
    private Duration duration;
    private String name;
    private String description;
    private int maxPeopleAmount;

    public Activity(LocalDateTime beginning, Duration duration, String name, String description, int maxPeopleAmount) {
        this.beginning = beginning;
        this.duration = duration;
        this.name = name;
        this.description = description;
        this.maxPeopleAmount = maxPeopleAmount;
    }

    public Activity(LocalDateTime beginning, Duration duration, String name, int maxPeopleAmount) {
        this.beginning = beginning;
        this.duration = duration;
        this.name = name;
        // FIXME(scrabsha): is this a good idea to provide a placeholder text
        // here?
        this.description = "Empty";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxPeopleAmount() {
        return maxPeopleAmount;
    }

    public void setMaxPeopleAmount(int maxPeopleAmount) {
        this.maxPeopleAmount = maxPeopleAmount;
    }
}
