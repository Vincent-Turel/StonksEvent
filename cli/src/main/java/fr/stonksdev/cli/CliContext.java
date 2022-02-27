package fr.stonksdev.cli;

import fr.stonksdev.cli.model.StonksEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CliContext {
    private Map<String, StonksEvent> events;

    public Map<String, StonksEvent> getEvents() {
        return events;
    }

    public CliContext() {
        events = new HashMap<>();
    }

    @Override
    public String toString() {
        return events.keySet().stream()
                .map(key -> key + "=" + events.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
