package fr.stonksdev.cli.commands;

import fr.stonksdev.cli.CliContext;
import fr.stonksdev.cli.model.StonksEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@ShellComponent
public class EventCommand {

    public static final String BASE_URI = "/events";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private CliContext cliContext;

    @ShellMethod("Create an event in the CoD backend (create-event EVENT_NAME NUMBER_OF_ATTENDEE START_DATE END_DATE)")
    public StonksEvent createEvent(String name, int poepleNb, Date startDate, Date endDate) {
        StonksEvent res = restTemplate.postForObject(BASE_URI+"/register",new StonksEvent(name, poepleNb, startDate, endDate),StonksEvent.class);
        cliContext.getEvents().put(res.getName(),res);
        return res;
    }

    @ShellMethod("List all events")
    public String events() {
        return cliContext.getEvents().toString();
    }

}
