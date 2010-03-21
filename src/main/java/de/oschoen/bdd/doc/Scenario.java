package de.oschoen.bdd.doc;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


public class Scenario {

    private static Logger logger = Logger.getLogger(Scenario.class.getName());

    private final String name;
    private final String origJavaSource;
    private final List<String> givens;
    private final List<String> whens;
    private final List<String> thens;
    private final String errorMsg; 

    public Scenario(String name, List<String> givens, List<String> whens, List<String> thens, String origJavaSource) {
        this(name, "", givens, whens, thens, origJavaSource);

    }

    public static Scenario createIncorrectScenario(String name, String errorMsg, String origJavaSource) {
        logger.warning(errorMsg);
        return new Scenario(name, errorMsg, Collections.<String>emptyList(), Collections.<String>emptyList(), Collections.<String>emptyList(), origJavaSource);    
    }

    private Scenario(String name, String errorMsg, List<String> givens, List<String> whens, List<String> thens, String origJavaSource) {
        this.errorMsg = errorMsg;
        this.name = name;
        this.givens = givens;

        if (whens != null) {
            this.whens = whens;
        } else {
            this.whens = new ArrayList<String>();
        }

        this.thens = thens;
        this.origJavaSource = origJavaSource;

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

    public String getOrigJavaSource() {
        return origJavaSource;
    }

    public boolean isIncorrect() {
        return !StringUtils.isEmpty(errorMsg);
    }

    public String getErrorMsg() {
        return errorMsg;
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
