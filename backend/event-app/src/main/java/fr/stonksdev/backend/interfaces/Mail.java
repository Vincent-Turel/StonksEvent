package fr.stonksdev.backend.interfaces;

public interface Mail {
    boolean send(String recipient, String object, String content);
}
