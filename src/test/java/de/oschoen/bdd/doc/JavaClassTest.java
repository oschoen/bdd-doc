package de.oschoen.bdd.doc;

import org.junit.Test;

import static org.junit.Assert.*;


public class JavaClassTest {

    @Test
    public void shouldHasTestMethods() {
        JavaClass testClass = new JavaClass(false, "pkg","TestClass", "BaseTestClass");
        testClass.addTestMethod(new TestMethod("shouldBeATestMethod", "ignore src"));

        assertTrue(testClass.hasTestMethods());
    }

    @Test
    public void shouldHasNoTestMethods() {
        JavaClass testClass = new JavaClass(false, "pkg","TestClass", "BaseTestClass");

        assertFalse(testClass.hasTestMethods());
    }
}
