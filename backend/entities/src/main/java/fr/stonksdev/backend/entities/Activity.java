package fr.stonksdev.backend.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Activity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String name;

    @NotNull
    private LocalDateTime beginning;

    @Embedded
    private Duration duration;

    @NotNull
    private int maxPeopleAmount;

    @ManyToOne
    private StonksEvent event;

    @ManyToOne
    private Room room;

    public Activity(LocalDateTime beginning, Duration duration, String name, int maxPeopleAmount, StonksEvent event) {
        this.beginning = beginning;
        this.duration = duration;
        this.name = name;
        this.maxPeopleAmount = maxPeopleAmount;
        this.event = event;
        this.room = null;
    }

    public Activity(LocalDateTime beginning, Duration duration, String name, StonksEvent event) {
        this.beginning = beginning;
        this.duration = duration;
        this.name = name;
        this.maxPeopleAmount = 9999;
        this.event = event;
        this.room = null;
    }

    public Activity() {

    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getEndDate(){
        return beginning.plusMinutes(duration.asMinutes());
    }

    public LocalDateTime getBeginning() {
        return beginning;
    }

    public void setBeginning(LocalDateTime newBeginning) {
        this.beginning = newBeginning;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration newDuration) {
        this.duration = newDuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public int getMaxPeopleAmount() {
        return maxPeopleAmount;
    }

    public void setMaxPeopleAmount(int newMaxPeopleAmount) {
        this.maxPeopleAmount = newMaxPeopleAmount;
    }

    public StonksEvent getEvent() {
        return event;
    }

    public void setEvent(StonksEvent event) {
        this.event = event;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public TimeSlot generateTimeSlot() {
        return new TimeSlot(this, beginning, duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        Activity that = (Activity) o;
        return getMaxPeopleAmount() == that.getMaxPeopleAmount()
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getDuration(), that.getDuration())
                && Objects.equals(getBeginning(), that.getBeginning());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getBeginning(), getDuration(), getMaxPeopleAmount());
    }
}
