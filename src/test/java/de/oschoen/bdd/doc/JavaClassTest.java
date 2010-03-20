package de.oschoen.bdd.doc;

import org.junit.Test;

import static org.junit.Assert.*;


public class JavaClassTest {

    @Test
    public void shouldHasTestMethods() {
        JavaClass testClass = new JavaClass(false, "TestClass", "BaseTestClass");
        testClass.addTestMethod(new TestMethod("shouldBeATestMethod"));

        assertTrue(testClass.hasTestMethods());
    }

    @Test
    public void shouldHasNoTestMethods() {
        JavaClass testClass = new JavaClass(false, "TestClass", "BaseTestClass");

        assertFalse(testClass.hasTestMethods());
    }
}
