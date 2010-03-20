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

        assertEquals("de.oschoen.bdd.doc.BaseExampleTestClazz", classHierarchy.getClass("de.oschoen.bdd.doc.BaseExampleTestClazz").getName());
        assertEquals("de.oschoen.bdd.doc.ExampleTestClazz", classHierarchy.getClass("de.oschoen.bdd.doc.ExampleTestClazz").getName());


    }

    @Test
    public void shouldOnlyFindTestMethods() {

        ClassHierarchy classHierarchy = bddProcessor.parseFiles(testDirectory.getTestFiles("ExampleTestClazz.java", "BaseExampleTestClazz.java"));

        Collection<TestMethod> testMethods = classHierarchy.getClass("de.oschoen.bdd.doc.ExampleTestClazz").getTestMethods();

        assertEquals(1, testMethods.size());

        thenEnsureTestMethodWithName(testMethods, "shouldBeAnotherExample");

    }

    @Test
    public void shouldOnlyFindMethodInvocationsStatements() {

        ClassHierarchy classHierarchy = bddProcessor.parseFiles(testDirectory.getTestFiles("ExampleTestClazz.java", "BaseExampleTestClazz.java"));

        Collection<TestMethod> testMethods = classHierarchy.getClass("de.oschoen.bdd.doc.ExampleTestClazz").getTestMethods();

        assertEquals(1, testMethods.size());

        thenEnsureTestMethodWithName(testMethods, "shouldBeAnotherExample");

    }

    public void shouldThrowExceptionBecauseTestMethodsWithParametersAreNotSupported() {
        ClassHierarchy classHierarchy = bddProcessor.parseFiles(testDirectory.getTestFiles("ExampleTestClazzWithMethodParam.java"));
        Collection<TestMethod> testMethods = classHierarchy.getClass("de.oschoen.bdd.doc.ExampleTestClazz").getTestMethods();

        assertEquals(0, testMethods.size());

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
