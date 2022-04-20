package fr.stonksdev.backend.components.exceptions;

public class RoomNotFoundException extends Exception{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoomNotFoundException() {
    }

    public RoomNotFoundException(String activityName) {
        this.name = activityName;
    }
}
