package de.oschoen.bdd.doc;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScenarioTest {

    @Test
    public void shouldTransformMethodNameToNaturalLanguage() {
        TestMethod testMethod = new TestMethod("shouldTransformMethodNameToNaturalLanguage");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIsRed"));

        assertEquals("transform method name to natural language", testMethod.getScenario().getName());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionIfTestMethodDontStartWithShould() {
        new TestMethod("dontStartsWithShould");
    }

    @Test
    public void shouldFindGivensFromTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIsRed"));

        Scenario scenario = testMethod.getScenario();

        assertEquals(1, scenario.getGivens().size());
        assertEquals("some thing", scenario.getGivens().get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionIfNoGivenStatementsExistisInTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenDoSomeThing"));
        Scenario scenario = testMethod.getScenario();
    }

    @Test
    public void shouldFindWhensWithoutParamsFromTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("whenUserDoSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIsRed"));

        Scenario scenario = testMethod.getScenario();
        assertEquals(1, scenario.getWhens().size());
        assertEquals("user do some thing", scenario.getWhens().get(0));
    }

    @Test
    public void shouldFindWhensWithParamsFromTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("whenUserDoSomeThing", new String[]{"param1", "param2"}));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIsRed"));

        Scenario scenario = testMethod.getScenario();
        assertEquals(1, scenario.getWhens().size());
        assertEquals("user do some thing param1 param2", scenario.getWhens().get(0));
    }

    @Test
    public void shouldFindThensWithoutParamsFromTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIsRed"));

        Scenario scenario = testMethod.getScenario();
        assertEquals(1, scenario.getThens().size());
        assertEquals("the answer is red", scenario.getThens().get(0));
    }

    @Test
    public void shouldFindThensWithParamsFromTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenTheAnswerIs", new String[]{"red"}));

        Scenario scenario = testMethod.getScenario();
        assertEquals(1, scenario.getThens().size());
        assertEquals("the answer is red", scenario.getThens().get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionIfNoThenStatementsExistisInTestMethod() {
        TestMethod testMethod = new TestMethod("shouldWorks");
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomeThing"));
        testMethod.addMethodInvocationStatement(new MethodInvocationStatement("whenDoSomeThing"));
        Scenario scenario = testMethod.getScenario();
    }

}
