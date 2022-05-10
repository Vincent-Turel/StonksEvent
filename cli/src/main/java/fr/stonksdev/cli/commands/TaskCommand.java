package fr.stonksdev.cli.commands;

import fr.stonksdev.cli.CliContext;
import fr.stonksdev.cli.model.TaskWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

@ShellComponent
public class TaskCommand {
    private static final String BASE_URI = "/tasks";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CliContext cliContext;

    @ShellMethod("List all tasks for a given event")
    public String getTasksOfEvent(String eventName) throws Exception {
        if (!cliContext.getEvents().containsKey(eventName)) {
            throw new Exception("The event '" + eventName + "' does not exist, please, use this command for only an existing event.");
        }
        Long eventId = cliContext.getEvents().get(eventName).id;
        return restTemplate.getForObject(BASE_URI + "/" + eventId, TaskWrapper.class).toString();
    }
}
