package de.oschoen.bdd.doc.report;

import de.oschoen.bdd.doc.Scenario;
import de.oschoen.bdd.doc.TestObject;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

public class TestObjectReporter {

    private final ScenarioReporter scenarioReport;

    public TestObjectReporter(ScenarioReporter scenarioReport) {
        this.scenarioReport = scenarioReport;
    }



    public String getReport(TestObject testObject) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html>");
        sb.append("<head><title>" + escapeHtml(testObject.getName()) + "</title></head>");
        sb.append("<body>");
        sb.append("<h1>" + escapeHtml(testObject.getName()) + "</h1>");

        for (Scenario scenario : testObject.getScenarios()) {
              sb.append(scenarioReport.getReport(scenario));
        }
        
        sb.append("</body>");

        return sb.toString();

    }
}
