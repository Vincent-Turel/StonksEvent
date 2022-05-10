package fr.stonksdev.cli.model;

import java.time.LocalDateTime;

/**
 * Represents a slot at which a specific activity is scheduled in a given room.
 * This is mostly useful for planning generation.<br>
 * <p>
 * This class is a DTO. This means that is has no behaviour, every field is
 * public and will stay like this.
 */
public class TimeSlot {
    public Activity activity;
    public LocalDateTime beginning;
    public Duration duration;

    public TimeSlot(Activity activity, LocalDateTime beginning, Duration duration) {
        this.activity = activity;
        this.beginning = beginning;
        this.duration = duration;
    }

    private TimeSlot() {
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "activityName='" + activity + '\'' +
                ", beginning='" + beginning + '\'' +
                ", duration=" + duration +
                '}';
    }
}
