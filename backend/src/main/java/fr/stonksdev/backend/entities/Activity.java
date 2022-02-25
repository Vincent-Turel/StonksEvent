package fr.stonksdev.backend.entities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Activity {
    private LocalDateTime beginning;
    private Duration duration;
    private String name;
    private String description;
    private Room room;
    private List<Equipment> requiredEquipment;
    private int maxPeopleAmount;

    public Activity(LocalDateTime beginning, Duration duration, String name, String description, Room room, List<Equipment> requiredEquipment, int maxPeopleAmount){
        this.beginning = beginning;
        this.duration = duration;
        this.name = name;
        this.description = description;
        this.room = room;
        this.requiredEquipment = requiredEquipment;
        this.maxPeopleAmount = maxPeopleAmount;
    }

    public LocalDateTime getBeginning(){
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

    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Equipment> getRequiredEquipment() {
        return requiredEquipment;
    }
    public void setRequiredEquipment(List<Equipment> requiredEquipment) {
        this.requiredEquipment = requiredEquipment;
    }

    public int getMaxPeopleAmount() {
        return maxPeopleAmount;
    }
    public void setMaxPeopleAmount(int maxPeopleAmount) {
        this.maxPeopleAmount = maxPeopleAmount;
    }
}
