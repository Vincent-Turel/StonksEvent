package fr.stonksdev.backend.components;

import fr.stonksdev.backend.entities.dto.MailDTO;
import fr.stonksdev.backend.interfaces.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;

@Component
public class MailProxy implements Mail {

    private static final Logger LOG = LoggerFactory.getLogger(MailProxy.class);

    @Value("${mail.host.baseurl}")
    private String mailUri;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean send(String recipient, String subject, String content) {
        int result = 1;
        try {
            result = restTemplate.postForObject(mailUri + "/send", new MailDTO(recipient, subject, content), Integer.class);
        } catch (Exception e) {
            LOG.info("Mail could not be sent because server was not responding at " + mailUri);
        }
        return result == 0;
    }
}
