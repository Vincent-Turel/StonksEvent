package fr.stonksdev.backend.components.interfaces;

public interface Mail {
    boolean send(String recipient, String object, String content);
}
