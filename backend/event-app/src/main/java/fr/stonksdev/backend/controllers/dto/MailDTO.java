package fr.stonksdev.backend.controllers.dto;

public class MailDTO {
    private String recipient;
    private String object;
    private String body;

    public MailDTO(String recipient, String object, String body) {
        this.recipient = recipient;
        this.object = object;
        this.body = body;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getObject() {
        return object;
    }

    public String getBody() {
        return body;
    }
}

