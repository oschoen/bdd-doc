package de.oschoen.bdd.doc;

import java.util.ArrayList;
import java.util.List;


public class Scenario {

    private final String name;
    private final List<String> givens;
    private final List<String> whens;
    private final List<String> thens;

    public Scenario(String name, List<String> givens, List<String> whens, List<String> thens) {
        this.name = name;
        this.givens = givens;

        if (whens != null) {
            this.whens = whens;
        } else {
            this.whens = new ArrayList<String>();
        }
        
        this.thens = thens;
    }


    public String getName() {
        return name;
    }

    public List<String> getGivens() {
        return givens;
    }

    public List<String> getWhens() {
        return whens;
    }

    public List<String> getThens() {
        return thens;
    }

    @Override
    public String toString() {
        return "Scenario{" +
                "name='" + name + '\'' +
                ", givens=" + givens +
                ", whens=" + whens +
                ", thens=" + thens +
                '}';
    }

 
}
