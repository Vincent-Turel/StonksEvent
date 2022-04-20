package fr.stonksdev.cli.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Activity {
    public LocalDateTime beginning;
    public Duration duration;
    public String name;
    public int maxPeopleAmount;
    public Long id;

    public Activity(LocalDateTime beginning, Duration duration, String name, int maxPeopleAmount) {
        this.beginning = beginning;
        this.duration = duration;
        this.name = name;
        this.maxPeopleAmount = maxPeopleAmount;
    }

    private Activity() {

    }

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", maxPeopleAmount=" + maxPeopleAmount +
                ", beginning=" + beginning +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        Activity activity = (Activity) o;
        return maxPeopleAmount == activity.maxPeopleAmount && Objects.equals(beginning, activity.beginning)
                && Objects.equals(duration, activity.duration) && Objects.equals(name, activity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginning, duration, name, maxPeopleAmount, id);
    }
}
