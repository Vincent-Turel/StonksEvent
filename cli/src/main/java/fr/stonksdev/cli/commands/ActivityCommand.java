package fr.stonksdev.cli.commands;

import fr.stonksdev.cli.CliContext;
import fr.stonksdev.cli.exceptions.EventDoesNotExistException;
import fr.stonksdev.cli.model.Activity;
import fr.stonksdev.cli.model.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import static fr.stonksdev.cli.commands.EventCommand.EVENT_BASE_URI;

@ShellComponent
public class ActivityCommand {
    public static final String ACTIVITY_BASE_URI = "/activities";


    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private CliContext cliContext;

    @ShellMethod("Create an activity (create-activity EVENT_NAME ACTIVITY_NAME MAX_NUMBER_PEOPLE START_DATE DURATION (in minutes))\n DATE FORMAT = dd/mm/yyyy hh:mm, DURATION FORMAT = positive integer")
    public Activity createActivity(String eventName, String activityName, int nbPeople, String start, int duration) throws DateTimeParseException, EventDoesNotExistException {
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        if (!cliContext.getEvents().containsKey(eventName)) {
            throw new EventDoesNotExistException("The event '" + eventName + "' does not exist, please, use this command for only an existing event.");
        }
        Long eventId = cliContext.getEvents().get(eventName).id;
        Activity activity = restTemplate.postForObject(EVENT_BASE_URI + "/" + eventId + ACTIVITY_BASE_URI, new Activity(startDate, Duration.ofMinutes(duration), activityName, nbPeople), Activity.class);
        cliContext.getActivities().put(activityName, activity);
        return activity;
    }

    @ShellMethod("Update an activity (update-activity EVENT_NAME ACTIVITY_NAME MAX_NUMBER_PEOPLE START_DATE DURATION (in minutes))\n DATE FORMAT = dd/mm/yyyy hh:mm, DURATION FORMAT = positive integer")
    public Activity updateActivity(String eventName, String activityName, int nbPeople, String start, int duration) throws Exception {
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        if (!cliContext.getEvents().containsKey(eventName)) {
            throw new Exception("The event '" + eventName + "' does not exist, please, use this command for only an existing event.");
        }
        if (!cliContext.getActivities().containsKey(activityName)) {
            throw new Exception("The activity '" + activityName + "' does not exist, please, use this command for only an existing activity.");
        }
        Long eventId = cliContext.getEvents().get(eventName).id;
        Long activityId = cliContext.getActivities().get(activityName).id;
        Activity activity = restTemplate.postForObject(EVENT_BASE_URI + "/" + eventId + ACTIVITY_BASE_URI + "/" + activityId, new Activity(startDate, Duration.ofMinutes(duration), activityName, nbPeople), Activity.class);
        cliContext.getActivities().put(activityName, activity);
        return activity;
    }

    @ShellMethod("List all activities from an event")
    public String activitiesFromEvent(String eventName) throws Exception {
        if (!cliContext.getEvents().containsKey(eventName)) {
            throw new Exception("The event '" + eventName + "' does not exist, please, use this command for only an existing event.");
        }
        Long eventId = cliContext.getEvents().get(eventName).id;
        return restTemplate.getForObject(EVENT_BASE_URI + "/" + eventId + ACTIVITY_BASE_URI, String.class);
    }

    @ShellMethod("List all activities")
    public String activities() {
        Map<String, Activity> activityMap = cliContext.getActivities();
        StringBuilder res = new StringBuilder();
        res.append("All activities :\n");
        int i = 1;
        for (Activity activity: activityMap.values()) {
            res.append(i).append(". ").append(activity.toString()).append("\n");
            i++;
        }
        return res.toString();
    }
}
