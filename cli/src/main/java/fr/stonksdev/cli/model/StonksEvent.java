package fr.stonksdev.cli.model;

import java.util.Date;

public class StonksEvent {

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String name;
    private int amountOfPeople;
    private Date startDate;
    private Date endDate;

    public StonksEvent(String name, int amountOfPeople, Date startDate, Date endDate)  {
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
