package fr.stonksdev.backend.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Room extends Place {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoomKind roomKind;

    @NotNull
    private int capacity;

    @OneToMany(mappedBy = "room")
    private Set<Activity> activities;

    public Room(String name, RoomKind roomKind, int capacity) {
        this.name = name;
        this.roomKind = roomKind;
        this.capacity = capacity;
        this.activities = new HashSet<>();
    }

    public Room() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoomKind getRoomKind() {
        return roomKind;
    }

    public void setRoomKind(RoomKind roomKind) {
        this.roomKind = roomKind;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void addActivity(Activity activity) {
        activity.setRoom(this);
        activities.add(activity);
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return getCapacity() == room.getCapacity() && Objects.equals(getName(), room.getName()) && getRoomKind() == room.getRoomKind() && Objects.equals(getActivities(), room.getActivities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getRoomKind(), getCapacity(), getActivities());
    }
}
