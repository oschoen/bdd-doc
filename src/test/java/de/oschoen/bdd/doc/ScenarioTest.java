package de.oschoen.bdd.doc;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class ScenarioTest {

    @Test
    public void shouldTransformMethodNameToNaturalLanguage() {
        TestMethod testMethod = new TestMethod("shouldTransformMethodNameToNaturalLanguage", "ignore src");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIsRed"));

        assertEquals("transform method name to natural language", testMethod.getScenario().getName());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionIfTestMethodDontStartWithShould() {
        new TestMethod("dontStartsWithShould", "ignore src");
    }

    @Test
    public void shouldFindGivensWithoutParamsFromTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks", "ignore src");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIsRed"));

        Scenario scenario = testMethod.getScenario();

        assertEquals(1, scenario.getGivens().size());
        assertEquals("some thing", scenario.getGivens().get(0));
    }

    @Test
    public void shouldReportCorrectScenarioIfNoErrorExists() {
        TestMethod testMethod = new TestMethod("shouldWorks", "ignore src");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIsRed"));

        Scenario scenario = testMethod.getScenario();

        assertFalse(scenario.isIncorrect());
    }

    @Test
    public void shouldFindGivensWithParamsFromTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks", "ignore src");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing", new String[]{"param1", "param2"}));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIsRed"));

        Scenario scenario = testMethod.getScenario();

        assertEquals(1, scenario.getGivens().size());
        assertEquals("some thing param1 param2", scenario.getGivens().get(0));
    }

    @Test
    public void shouldCreateScenarioWithErrorMsgIfNoGivenStatementsExistisInTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks", "ignore src");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenDoSomeThing"));
        Scenario scenario = testMethod.getScenario();

        assertTrue(scenario.isIncorrect());
    }

    @Test
    public void shouldFindWhensWithoutParamsFromTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks", "ignore src");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("whenUserDoSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIsRed"));

        Scenario scenario = testMethod.getScenario();
        assertEquals(1, scenario.getWhens().size());
        assertEquals("user do some thing", scenario.getWhens().get(0));
    }

    @Test
    public void shouldFindWhensWithParamsFromTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks", "ignore src");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("whenUserDoSomeThing", new String[]{"param1", "param2"}));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIsRed"));

        Scenario scenario = testMethod.getScenario();
        assertEquals(1, scenario.getWhens().size());
        assertEquals("user do some thing param1 param2", scenario.getWhens().get(0));
    }

    @Test
    public void shouldFindThensWithoutParamsFromTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks", "ignore src");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIsRed"));

        Scenario scenario = testMethod.getScenario();
        assertEquals(1, scenario.getThens().size());
        assertEquals("the answer is red", scenario.getThens().get(0));
    }

    @Test
    public void shouldFindThensWithParamsFromTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks", "ignore src");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIs", new String[]{"red"}));

        Scenario scenario = testMethod.getScenario();
        assertEquals(1, scenario.getThens().size());
        assertEquals("the answer is red", scenario.getThens().get(0));
    }

    @Test
    public void shouldCreateScenarioWithErrorMsgIfNoThenStatementsExistisInTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks", "ignore src");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("whenDoSomeThing"));
        Scenario scenario = testMethod.getScenario();

        assertTrue(scenario.isIncorrect());

    }

    @Test
    public void shouldHaveOriginalJavaSourceCodeFromTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks", "the original java source code.");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIs", new String[]{"red"}));

        Scenario scenario = testMethod.getScenario();
        assertEquals("the original java source code.", scenario.getOrigJavaSource());
    }

    @Test
    public void shouldCreateIncorrectScenarioIfErrorMsgAdded() {
        Scenario scenario = Scenario.createIncorrectScenario("name", "errorMsg", "ignore src");

        assertTrue(scenario.isIncorrect());
    }

    @Test
    public void shouldProvideErrorMsgIfScenarioIsIncorrect() {
        Scenario scenario = Scenario.createIncorrectScenario("name", "errorMsg", "ignore src");

        assertEquals("errorMsg", scenario.getErrorMsg());
    }

    @Test
    public void shouldLogWarningIfIncorrectScenarioFound() {

        Logger logger = Logger.getLogger(Scenario.class.getName());

        final AtomicInteger numberOfWarnings = new AtomicInteger();

        Handler handler = new Handler() {

            @Override
            public void publish(LogRecord record) {
                if (record.getLevel() == Level.WARNING) {
                    numberOfWarnings.incrementAndGet();
                }
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        };

        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);

        Scenario.createIncorrectScenario("name", "errorMsg", "ignore src");

        assertEquals(1, numberOfWarnings.intValue());

    }

}
