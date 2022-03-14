package fr.stonksdev.backend.controllers.dto;

public class MailDTO {
    private String recipient;
    private String subject;
    private String body;

    public MailDTO(String recipient, String subject, String body) {
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}

