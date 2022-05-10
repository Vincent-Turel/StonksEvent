package fr.stonksdev.backend.components.exceptions;

public class ImpossibleCreationException extends Exception {
    private String id;

    public ImpossibleCreationException(String id) {
        this.id = id;
    }

    public ImpossibleCreationException() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
