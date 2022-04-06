package fr.stonksdev.backend.components.exceptions;

public class EventIdNotFoundException extends Exception {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventIdNotFoundException() {
    }

    public EventIdNotFoundException(String customerName) {
        this.name = customerName;
    }
}
