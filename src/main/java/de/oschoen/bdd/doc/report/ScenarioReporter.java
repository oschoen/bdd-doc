package de.oschoen.bdd.doc.report;

import de.oschoen.bdd.doc.Scenario;
import org.apache.commons.lang.StringUtils;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;


public class ScenarioReporter {
    
    public String getReport(Scenario scenario) {
        StringBuilder out = new StringBuilder();
        out.append("<h2>").append(escapeHtml(scenario.getName())).append("</h2>");

        out.append("<p><strong>Given </strong> " + StringUtils.join(scenario.getGivens(), " <br> and ") + "</p>");

        if (scenario.getWhens().size() > 0) {
            out.append("<p><strong>When </strong> " + StringUtils.join(scenario.getWhens(), " <br> and ") + "</p>");

        }

        out.append("<p><strong>Then </strong> " + StringUtils.join(scenario.getThens(), " <br> and ") + "</p>");

        out.append("<pre>" + escapeHtml(scenario.getOrigJavaSource()) + "</pre>");
        return out.toString();
    }
}
