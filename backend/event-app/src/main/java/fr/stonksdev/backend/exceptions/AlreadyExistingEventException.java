package fr.stonksdev.backend.exceptions;

public class AlreadyExistingEventException extends Exception {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlreadyExistingEventException() {
    }

    public AlreadyExistingEventException(String eventName) {
        this.name = eventName;
    }
}
