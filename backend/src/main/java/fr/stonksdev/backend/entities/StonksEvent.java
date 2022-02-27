package fr.stonksdev.backend.entities;

import java.util.List;

public class StonksEvent {
    private String name;
    private int amountOfPeople;
    private List<Activity> activities;

    public StonksEvent(String name, int amountOfPeople, List<Activity> activities)  {
        this.name = name;
        this.amountOfPeople = amountOfPeople;
        this.activities = activities;
    }

}
