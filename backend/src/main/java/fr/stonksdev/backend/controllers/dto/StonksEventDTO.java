package fr.stonksdev.backend.controllers.dto;

import fr.stonksdev.backend.entities.Activity;

import java.util.Date;
import java.util.List;

public class StonksEventDTO {

    private String id;
    private String name;
    private int amountOfPeople;
    private Date startDate;
    private Date endDate;

    public StonksEventDTO(String id, String name, int amountOfPeople, Date startDate, Date endDate)  {
        this.id = id;
        this.name = name;
        this.amountOfPeople = amountOfPeople;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmountOfPeople() {
        return amountOfPeople;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmountOfPeople(int amountOfPeople) {
        this.amountOfPeople = amountOfPeople;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "StonksEvent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", amountOfPeople=" + amountOfPeople +
                '}';
    }

}
