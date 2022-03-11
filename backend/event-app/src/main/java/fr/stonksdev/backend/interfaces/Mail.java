package fr.stonksdev.backend.interfaces;

public interface Mail {

    public boolean send(String recipient, String object, String content);
}
