package de.oschoen.bdd.doc.report;

import de.oschoen.bdd.doc.Scenario;
import de.oschoen.bdd.doc.TestObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class TestObjectReporterTest {

    @Test
    public void shouldHasTestObjectNameInHtmlTitle() {
        TestObjectReporter testObjectReport = new TestObjectReporter(new ScenarioReporter());

        assertThat(testObjectReport.getReport(new TestObject("pkg","Test Object Name")), containsString(("<title>pkg.Test Object Name</title>")));

    }


    @Test
    public void shouldHasTestObjectNameInHtmlH1Tag() {
        TestObjectReporter testObjectReport = new TestObjectReporter(new ScenarioReporter());
        assertThat(testObjectReport.getReport(new TestObject("pkg","Test Object Name")), containsString(("<h1>pkg.Test Object Name</h1>")));


    }

    @Test
    public void shouldEscapeHtml() {
        TestObjectReporter testObjectReport = new TestObjectReporter(new ScenarioReporter());

        assertThat(testObjectReport.getReport(new TestObject("pkg","Test Object <Name>")), containsString(("<title>pkg.Test Object &lt;Name&gt;</title>")));
        assertThat(testObjectReport.getReport(new TestObject("pkg","Test Object <Name>")), containsString(("<h1>pkg.Test Object &lt;Name&gt;</h1>")));
    }

    @Test
    public void shouldIncludeScenarioReport() {
        TestObjectReporter testObjectReport = new TestObjectReporter(new ScenarioReporter() {
            @Override
            public String getReport(Scenario scenario) {
                return "Scenario Example Report";
            }
        });
        TestObject testObject = new TestObject("pkg","Test Object Name");
        testObject.addScenario(new Scenario("Scenario name", new ArrayList<String>(), null, null, "ignore src"));
        assertThat(testObjectReport.getReport(testObject), containsString(("Scenario Example Report")));

    }

    @Test
    public void shouldIncludeAllScenarioReports() {

        final AtomicInteger scenarioCounter = new AtomicInteger();

        TestObjectReporter testObjectReport = new TestObjectReporter(new ScenarioReporter() {

            @Override
            public String getReport(Scenario scenario) {
                return "Scenario Example Report " + scenarioCounter.incrementAndGet();
            }
        });

        TestObject testObject = new TestObject("pkg","Test Object Name");
        testObject.addScenario(new Scenario("Scenario name", new ArrayList<String>(), null, null, "ignore src"));
        testObject.addScenario(new Scenario("Scenario name", new ArrayList<String>(), null, null, "ignore src"));

        String report = testObjectReport.getReport(testObject);

        assertThat(report, containsString("Scenario Example Report 1"));
        assertThat(report, containsString("Scenario Example Report 2"));


    }
}
