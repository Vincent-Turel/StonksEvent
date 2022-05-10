package fr.stonksdev.cli.model;

import java.util.List;

public class ActivityWrapper {
    public List<Activity> list;

    public ActivityWrapper(List<Activity> list) {
        this.list = list;
    }

    public ActivityWrapper(){

    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Wrapper{set=");
        for (Activity element: list ) {
            res.append(element.toString()).append("\n");
        }
        res.append("}");
        return res.toString();
    }
}
