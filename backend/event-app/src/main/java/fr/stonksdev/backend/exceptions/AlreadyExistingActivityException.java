package fr.stonksdev.backend.exceptions;

public class AlreadyExistingActivityException extends Exception {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlreadyExistingActivityException() {
    }

    public AlreadyExistingActivityException(String activityName) {
        this.name = activityName;
    }
}
