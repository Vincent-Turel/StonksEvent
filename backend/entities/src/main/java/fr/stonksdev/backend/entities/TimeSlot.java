package fr.stonksdev.backend.entities;

import java.time.LocalDateTime;

/**
 * Represents a slot at which a specific activity is scheduled in a given room.
 * This is mostly useful for planning generation.<br>
 *
 * This class is a DTO. This means that is has no behaviour, every field is
 * public and will stay like this.
 */
public class TimeSlot {
    public String activityName;
    public LocalDateTime beginning;
    public Duration duration;

    public TimeSlot(String activityName, LocalDateTime beginning, Duration duration) {
        this.activityName = activityName;
        this.beginning = beginning;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeSlot timeSlot = (TimeSlot) o;

        if (!activityName.equals(timeSlot.activityName)) return false;
        if (!beginning.equals(timeSlot.beginning)) return false;
        return duration.equals(timeSlot.duration);
    }

    @Override
    public int hashCode() {
        int result = activityName.hashCode();
        result = 31 * result + beginning.hashCode();
        result = 31 * result + duration.hashCode();
        return result;
    }
}
