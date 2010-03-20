package de.oschoen.bdd.doc;

import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ClassHierarchyTest {

    @Test
    public void shouldGetTheRightClassBack() {
        ClassHierarchy classHierarchy = new ClassHierarchy();
        classHierarchy.addClass(new JavaClass(true, "TestClass", "extendsFrom"));

        JavaClass javaClass = classHierarchy.getClass("TestClass");

        assertEquals("TestClass", javaClass.getName());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionIfTwoClassesWithSameNameExists() {
        ClassHierarchy classHierarchy = new ClassHierarchy();
        classHierarchy.addClass(new JavaClass(false, "TestClass", "extendsFrom"));
        classHierarchy.addClass(new JavaClass(true, "TestClass", "extendsFrom"));
    }

    @Test
    public void shouldFindTestMethodsOnASimpleClass() {
        ClassHierarchy classHierarchy = new ClassHierarchy();
        JavaClass javaClass = new JavaClass(false, "TestClass", null);
        javaClass.addTestMethod(new TestMethod("shouldBeAestMethod","ignore src"));
        classHierarchy.addClass(javaClass);

        assertEquals(1, classHierarchy.getTestMethodsForClass("TestClass").size());
        thenEnsureTestMethodWithName(classHierarchy.getTestMethodsForClass("TestClass"), "shouldBeAestMethod");

    }

    @Test
    public void shouldFindTestMethodsOnAExtendedClassWithoutAccessToTheParentClass() {
        ClassHierarchy classHierarchy = new ClassHierarchy();
        JavaClass javaClass = new JavaClass(false, "TestClass", "BaseTestClassWhichSourceNotExists");
        javaClass.addTestMethod(new TestMethod("shouldBeATestMethod", "ignore src"));
        classHierarchy.addClass(javaClass);

        assertEquals(1, classHierarchy.getTestMethodsForClass("TestClass").size());
        thenEnsureTestMethodWithName(classHierarchy.getTestMethodsForClass("TestClass"), "shouldBeATestMethod");

    }

    @Test
    public void shouldFindOverridenTestMethods() {
        ClassHierarchy classHierarchy = new ClassHierarchy();
        JavaClass testClass = new JavaClass(false, "TestClass", "BaseTestClass");
        TestMethod testClassTestMethod = new TestMethod("shouldBeATestMethod", "ignore src");
        testClassTestMethod.addMethodInvocationStatement(new MethodInvocationStatement("testStatement"));
        testClass.addTestMethod(testClassTestMethod);
        classHierarchy.addClass(testClass);

        JavaClass baseTestClass = new JavaClass(false, "BaseTestClass", null);
        TestMethod baseTestClassTestMethod = new TestMethod("shouldBeATestMethod", "ignore src");
        testClassTestMethod.addMethodInvocationStatement(new MethodInvocationStatement("testBaseStatement"));

        baseTestClass.addTestMethod(baseTestClassTestMethod);
        classHierarchy.addClass(baseTestClass);

        assertEquals(1, classHierarchy.getTestMethodsForClass("TestClass").size());
        thenEnsureTestMethodWithNameAndStatement(classHierarchy.getTestMethodsForClass("TestClass"), "shouldBeATestMethod", new MethodInvocationStatement("testStatement"));

    }

    @Test
    public void shouldFindTestMethodsAlsoFromAExtendedClass() {
        ClassHierarchy classHierarchy = new ClassHierarchy();
        JavaClass testClass = new JavaClass(false, "TestClass", "BaseTestClass");
        testClass.addTestMethod(new TestMethod("shouldBeATestMethod1", "ignore src"));
        classHierarchy.addClass(testClass);

        JavaClass baseTestClass = new JavaClass(false, "BaseTestClass", null);
        baseTestClass.addTestMethod(new TestMethod("shouldBeATestMethod2", "ignore src"));
        classHierarchy.addClass(baseTestClass);

        assertEquals(2, classHierarchy.getTestMethodsForClass("TestClass").size());
        thenEnsureTestMethodWithName(classHierarchy.getTestMethodsForClass("TestClass"), "shouldBeATestMethod1");
        thenEnsureTestMethodWithName(classHierarchy.getTestMethodsForClass("TestClass"), "shouldBeATestMethod2");

    }

    @Test
    public void shouldFindTestMethodsWithTheOrderOfOccurrence() {
        ClassHierarchy classHierarchy = new ClassHierarchy();
        JavaClass testClass = new JavaClass(false, "TestClass", "BaseTestClass");
        testClass.addTestMethod(new TestMethod("shouldBeATestMethod1", "ignore src"));
        testClass.addTestMethod(new TestMethod("shouldBeATestMethod2", "ignore src"));

        classHierarchy.addClass(testClass);
        List<TestMethod> testMethods = classHierarchy.getTestMethodsForClass("TestClass");

        assertEquals("shouldBeATestMethod1", testMethods.get(0).getName());
        assertEquals("shouldBeATestMethod2", testMethods.get(1).getName());

    }

    @Test
    public void shouldOnlyReturnAbstractClasses() {
        ClassHierarchy classHierarchy = new ClassHierarchy();
        JavaClass testClass = new JavaClass(false, "TestClass", "BaseTestClass");
        classHierarchy.addClass(testClass);

        JavaClass baseTestClass = new JavaClass(true, "BaseTestClass", null);
        classHierarchy.addClass(baseTestClass);

        assertEquals(1, classHierarchy.getAllNoAbstractClasses().size());
        assertEquals("TestClass", classHierarchy.getAllNoAbstractClasses().get(0).getName());

    }

    private void thenEnsureTestMethodWithName(Collection<TestMethod> testMethods, String name) {

        for (TestMethod testMethod : testMethods) {
            if (name.equals(testMethod.getName())) {
                return;
            }
        }

        fail("Cant find test method with name [" + name + "].");

    }

    private void thenEnsureTestMethodWithNameAndStatement(Collection<TestMethod> testMethods, String name, MethodInvocationStatement statement) {

        for (TestMethod testMethod : testMethods) {
            if (name.equals(testMethod.getName())) {
                if (testMethod.getMethodInvocationStatements().contains(statement)) {
                    return;
                }
            }
        }

        fail("Cant find test method with name [" + name + "] and statement [" + statement + "].");

    }
}
