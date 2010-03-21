package de.oschoen.bdd.doc;

import de.oschoen.bdd.doc.test.TestDirectory;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class BddProcessorTest {

    private TestDirectory testDirectory = new TestDirectory("testClasses");
    private BddProcessor bddProcessor = new BddProcessor();

    @Test
    public void shouldFindAllClasses() {

        ClassHierarchy classHierarchy = bddProcessor.parseFiles(testDirectory.getTestFiles("ExampleTestClazz.java", "BaseExampleTestClazz.java"));

        assertEquals("testClasses.BaseExampleTestClazz", classHierarchy.getClass("testClasses.BaseExampleTestClazz").getName());
        assertEquals("testClasses.ExampleTestClazz", classHierarchy.getClass("testClasses.ExampleTestClazz").getName());


    }

    @Test
    public void shouldOnlyFindTestMethods() {

        ClassHierarchy classHierarchy = bddProcessor.parseFiles(testDirectory.getTestFiles("ExampleTestClazz.java", "BaseExampleTestClazz.java"));

        Collection<TestMethod> testMethods = classHierarchy.getClass("testClasses.ExampleTestClazz").getTestMethods();

        assertEquals(1, testMethods.size());

        thenEnsureTestMethodWithName(testMethods, "shouldBeAnotherExample");

    }

    @Test
    public void shouldOnlyFindMethodInvocationsStatements() {

        ClassHierarchy classHierarchy = bddProcessor.parseFiles(testDirectory.getTestFiles("ExampleTestClazz.java", "BaseExampleTestClazz.java"));

        Collection<TestMethod> testMethods = classHierarchy.getClass("testClasses.ExampleTestClazz").getTestMethods();

        assertEquals(1, testMethods.size());

        thenEnsureTestMethodWithName(testMethods, "shouldBeAnotherExample");

    }

    @Test
    public void shouldIgnoreTestMethodsWithParameters() {
        ClassHierarchy classHierarchy = bddProcessor.parseFiles(testDirectory.getTestFiles("ExampleTestClazzWithMethodParam.java"));
        Collection<TestMethod> testMethods = classHierarchy.getClass("testClasses.ExampleTestClazzWithMethodParam").getTestMethods();

        assertEquals(1, testMethods.size());
        thenEnsureTestMethodWithName(testMethods, "shouldWork");

    }

     @Test
    public void shouldIgnoreAnonymousClasses() {
        ClassHierarchy classHierarchy = bddProcessor.parseFiles(testDirectory.getTestFiles("ExampleTestClazzWithAnonymousClass.java"));
        Collection<TestMethod> testMethods = classHierarchy.getClass("testClasses.ExampleTestClazzWithAnonymousClass").getTestMethods();

        assertEquals(1, testMethods.size());
        thenEnsureTestMethodWithName(testMethods, "shouldWork");

    }

    private void thenEnsureTestMethodWithName(Collection<TestMethod> testMethods, String name) {
        for (TestMethod testMethod : testMethods) {
            if (name.equals(testMethod.getName())) {
                return;
            }
        }

        fail("Cant find test method with name [" + name + "] in the actual list of methods [" + testMethods + "].");
    }
}
