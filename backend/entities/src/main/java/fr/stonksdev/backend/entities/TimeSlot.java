package fr.stonksdev.backend.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a slot at which a specific activity is scheduled in a given room.
 * This is mostly useful for planning generation.<br>
 * <p>
 * This class is a DTO. This means that is has no behaviour, every field is
 * public and will stay like this.
 */
@Entity
public class TimeSlot implements Comparable<TimeSlot> {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Activity activity;
    private LocalDateTime beginning;
    private Duration duration;

    @ManyToOne
    private Planning planning;

    public TimeSlot() {}

    public TimeSlot(Activity activity, LocalDateTime beginning, Duration duration) {
        this.activity = activity;
        this.beginning = beginning;
        this.duration = duration;
    }

    public LocalDateTime end() {
        return beginning.plusMinutes(duration.asMinutes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeSlot timeSlot = (TimeSlot) o;

        if (!activity.equals(timeSlot.activity)) return false;
        if (!beginning.equals(timeSlot.beginning)) return false;
        return duration.equals(timeSlot.duration);
    }

    @Override
    public int hashCode() {
        int result = activity.hashCode();
        result = 31 * result + beginning.hashCode();
        result = 31 * result + duration.hashCode();
        return result;
    }

    @Override
    public int compareTo(TimeSlot timeSlot) {
        return beginning.compareTo(timeSlot.beginning);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getBeginning() {
        return beginning;
    }
}
