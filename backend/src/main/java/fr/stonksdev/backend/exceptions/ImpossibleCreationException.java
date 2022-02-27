package fr.stonksdev.backend.exceptions;

public class NotCreatableException extends Exception {
    private String id;

    public NotCreatableException(String id) {
        this.id = id;
    }

    public NotCreatableException() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
