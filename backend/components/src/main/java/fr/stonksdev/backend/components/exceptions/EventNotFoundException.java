package fr.stonksdev.backend.components.exceptions;

public class EventNotFoundException extends Exception {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventNotFoundException() {
    }

    public EventNotFoundException(String customerName) {
        this.name = customerName;
    }
}
