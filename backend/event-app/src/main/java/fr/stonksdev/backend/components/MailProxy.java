package fr.stonksdev.backend.components;

import fr.stonksdev.backend.controllers.dto.MailDTO;
import fr.stonksdev.backend.interfaces.Mail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MailProxy implements Mail {

    @Value("${mail.host.baseurl}")
    private String mailUri;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean send(String recipient, String subject, String content) {
        int result = restTemplate.postForObject(mailUri + "/send", new MailDTO(recipient, subject, content), Integer.class);
        return result == 0;
    }
}
