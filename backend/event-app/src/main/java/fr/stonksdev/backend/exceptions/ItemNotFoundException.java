package fr.stonksdev.backend.exceptions;

public class ItemNotFoundException extends Exception {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemNotFoundException() {
    }

    public ItemNotFoundException(String customerName) {
        this.name = customerName;
    }
}
