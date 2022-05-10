package fr.stonksdev.cli.model;

import java.util.List;

public class StonksEventWrapper {
    public List<StonksEvent> list;

    public StonksEventWrapper(List<StonksEvent> list) {
        this.list = list;
    }

    public StonksEventWrapper(){

    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Wrapper{set=");
        for (StonksEvent element: list ) {
            res.append(element.toString()).append("\n");
        }
        res.append("}");
        return res.toString();
    }
}
