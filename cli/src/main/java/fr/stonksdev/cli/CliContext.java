package fr.stonksdev.cli;

import fr.stonksdev.cli.model.Activity;
import fr.stonksdev.cli.model.ActivityWrapper;
import fr.stonksdev.cli.model.StonksEvent;
import fr.stonksdev.cli.model.StonksEventWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static fr.stonksdev.cli.commands.ActivityCommand.ACTIVITY_BASE_URI;
import static fr.stonksdev.cli.commands.EventCommand.EVENT_BASE_URI;

@Component
public class CliContext {

    @Autowired
    RestTemplate restTemplate;

    private Map<String, StonksEvent> events;

    public Map<String, StonksEvent> getEvents() {
        return events;
    }

    private Map<String, Activity> activities;

    public Map<String, Activity> getActivities() {
        return activities;
    }

    public CliContext() {
        events = new HashMap<>();
        activities = new HashMap<>();
    }

    @PostConstruct
    private void fillContextWithDatabase(){
        if(restTemplate == null){
            System.out.println("cli context - fillContextWithDatabase: Rest template NULL !");
        }
        else{
            StonksEventWrapper stonksEventWrapper = restTemplate.getForObject(EVENT_BASE_URI, StonksEventWrapper.class);
            assert stonksEventWrapper != null;
            if(stonksEventWrapper.list != null){
                List<StonksEvent> stonksEventlist = stonksEventWrapper.list;
                for (StonksEvent event : stonksEventlist) {
                    events.put(event.name, event);
                }
            }

            ActivityWrapper activityWrapper = restTemplate.getForObject(ACTIVITY_BASE_URI, ActivityWrapper.class);
            assert activityWrapper != null;
            if( activityWrapper.list != null){
                List<Activity> activityList = activityWrapper.list;
                for (Activity activity : activityList) {
                    activities.put(activity.name, activity);
                }
            }
        }
    }

    @Override
    public String toString() {
        return events.keySet().stream()
                .map(key -> key + "=" + events.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
