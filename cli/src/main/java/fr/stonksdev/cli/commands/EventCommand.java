package fr.stonksdev.cli.commands;

import fr.stonksdev.cli.CliContext;
import fr.stonksdev.cli.model.Activity;
import fr.stonksdev.cli.model.StonksEvent;
import fr.stonksdev.cli.model.StonksEventWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@ShellComponent
public class EventCommand {

    public static final String EVENT_BASE_URI = "/events";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CliContext cliContext;

    @ShellMethod("Create an event (create-event EVENT_NAME NUMBER_OF_ATTENDEE START_DATE END_DATE)\n DATE FORMAT = dd/mm/yyyy hh:mm")
    public StonksEvent createEvent(String name, int poepleNb, String start, String end) throws DateTimeParseException {
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);

        StonksEvent event = restTemplate.postForObject(EVENT_BASE_URI, new StonksEvent(name, poepleNb, startDate, endDate), StonksEvent.class);
        cliContext.getEvents().put(name, event);
        return event;
    }

    @ShellMethod("Update an event (update-event EVENT_NAME NUMBER_OF_ATTENDEE START_DATE END_DATE)\n DATE FORMAT = dd/mm/yyyy hh:mm \n Constraint : same event name")
    public StonksEvent updateEvent(String name, int poepleNb, String start, String end) throws Exception {
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        if (!cliContext.getEvents().containsKey(name)) {
            throw new Exception("The event '" + name + "' does not exist, please, use this command for only an existing event.");
        }
        Long eventId = cliContext.getEvents().get(name).id;
        StonksEvent event = restTemplate.postForObject(EVENT_BASE_URI + "/" + eventId, new StonksEvent(name, poepleNb, startDate, endDate), StonksEvent.class);
        cliContext.getEvents().put(name, event);
        return event;
    }

    @ShellMethod("List all events")
    public String events() {
        Map<String, StonksEvent> stonksEventMap = cliContext.getEvents();
        StringBuilder res = new StringBuilder();
        res.append("All events :\n");
        int i = 1;
        for (StonksEvent event: stonksEventMap.values()) {
            res.append(i).append(". ").append(event.toString()).append("\n");
            i++;
        }
        return res.toString();
    }
}
