package fr.stonksdev.backend.components.exceptions;

public class WrongInputException extends Exception {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WrongInputException() {
    }

    public WrongInputException(String customerName) {
        this.name = customerName;
    }
}
