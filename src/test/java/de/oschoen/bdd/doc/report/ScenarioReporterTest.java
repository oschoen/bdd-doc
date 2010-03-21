package de.oschoen.bdd.doc.report;

import de.oschoen.bdd.doc.Scenario;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;


public class ScenarioReporterTest {

    @Test
    public void shouldWriteScenarioNameInHeading() {
        ScenarioReporter scenarioReporter = new ScenarioReporter();
        Scenario scenario = new Scenario("Test Scenario", new ArrayList<String>(), null, null,"ignore src");

        assertThat(scenarioReporter.getReport(scenario), containsString("<h2>Test Scenario</h2>"));
    }

    @Test
    public void shouldEscapeHtml() {
        ScenarioReporter scenarioReporter = new ScenarioReporter();
        Scenario scenario = new Scenario("Test & <Scenario>", new ArrayList<String>(), null, null,"ignore src");

        assertThat(scenarioReporter.getReport(scenario), containsString("Test &amp; &lt;Scenario&gt;"));

    }

    @Test
    public void shouldWriteGiven() {
        ScenarioReporter scenarioReporter = new ScenarioReporter();
        List<String> givens = new ArrayList<String>();
        givens.add("A precondition");

        Scenario scenario = new Scenario("Test Scenario", givens, null, null,"ignore src");

        assertThat(scenarioReporter.getReport(scenario), containsString("A precondition"));

    }

    @Test
    public void shouldConcatGivens() {
        ScenarioReporter scenarioReporter = new ScenarioReporter();
        List<String> givens = new ArrayList<String>();
        givens.add("A precondition 1");
        givens.add("A precondition 2");


        Scenario scenario = new Scenario("Test Scenario", givens, null, null,"ignore src");

        assertThat(scenarioReporter.getReport(scenario), containsString("A precondition 1 <br> and A precondition 2"));

    }

    @Test
    public void shouldWriteWhen() {
        ScenarioReporter scenarioReporter = new ScenarioReporter();
        List<String> givens = new ArrayList<String>();
        List<String> whens = new ArrayList<String>();
        whens.add("a action");

        Scenario scenario = new Scenario("Test Scenario", givens, whens, null,"ignore src");

        assertThat(scenarioReporter.getReport(scenario), containsString("a action"));

    }

    @Test
    public void shouldConcatWhens() {
        ScenarioReporter scenarioReporter = new ScenarioReporter();
        List<String> givens = new ArrayList<String>();
        List<String> whens = new ArrayList<String>();
        whens.add("a action 1");
        whens.add("a action 2");


        Scenario scenario = new Scenario("Test Scenario", givens, whens, null,"ignore src");

        assertThat(scenarioReporter.getReport(scenario), containsString("a action 1 <br> and a action 2"));

    }

    @Test
    public void shouldWriteThen() {
        ScenarioReporter scenarioReporter = new ScenarioReporter();
        List<String> givens = new ArrayList<String>();
        List<String> thens = new ArrayList<String>();
        thens.add("a assert");

        Scenario scenario = new Scenario("Test Scenario", givens, null, thens,"ignore src");

        assertThat(scenarioReporter.getReport(scenario), containsString("a assert"));

    }

    @Test
    public void shouldConcatThens() {
        ScenarioReporter scenarioReporter = new ScenarioReporter();
        List<String> givens = new ArrayList<String>();
        List<String> thens = new ArrayList<String>();
        thens.add("a assert 1");
        thens.add("a assert 2");


        Scenario scenario = new Scenario("Test Scenario", givens, null, thens,"ignore src");

        assertThat(scenarioReporter.getReport(scenario), containsString("a assert 1 <br> and a assert 2"));

    }

    @Test
    public void shouldWrtiteOriginalJavaSourceCode() {
        ScenarioReporter scenarioReporter = new ScenarioReporter();
        List<String> givens = new ArrayList<String>();
        givens.add("A precondition");

        Scenario scenario = new Scenario("Test Scenario", givens, null, null,"the original java source code.");

        assertThat(scenarioReporter.getReport(scenario), containsString("the original java source code."));
        
    }

    @Test
    public void shouldWriteErrorMsgIfScenarioIsIncorrect() {
        ScenarioReporter scenarioReporter = new ScenarioReporter();
        Scenario incorrectScenario = Scenario.createIncorrectScenario("name", "errorMsg", "ignore src");

        assertThat(scenarioReporter.getReport(incorrectScenario), containsString("errorMsg"));

    }
}
