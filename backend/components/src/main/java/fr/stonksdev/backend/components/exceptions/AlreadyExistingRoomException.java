package fr.stonksdev.backend.components.exceptions;

public class AlreadyExistingRoomException extends Exception {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlreadyExistingRoomException() {
    }

    public AlreadyExistingRoomException(String eventName) {
        this.name = eventName;
    }
}
