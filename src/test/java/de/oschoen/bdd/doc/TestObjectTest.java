package de.oschoen.bdd.doc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestObjectTest {

    @Test
    public void shouldUseOnlyNoAbstractClasses() {
        ClassHierarchy classHierarchy = new ClassHierarchy();

        JavaClass abstractJavaClass = new JavaClass(true, "AbstractClass", "");
        abstractJavaClass.addTestMethod(new TestMethod("shouldNotUsed","ignore src"));

        classHierarchy.addClass(abstractJavaClass);

        int numberOfTestObjects = TestObject.getAllTestObjects(classHierarchy).size();
        assertEquals(0, numberOfTestObjects);
    }

    @Test
    public void shouldUseOnlyTestClassesWithTestMethods() {
        ClassHierarchy classHierarchy = new ClassHierarchy();

        JavaClass javaClass = new JavaClass(false, "TestClass", "");
        classHierarchy.addClass(javaClass);

        int numberOfTestObjects = TestObject.getAllTestObjects(classHierarchy).size();
        assertEquals(0, numberOfTestObjects);
    }

     @Test
    public void shouldUseTestClassesWithTestMethods() {
        ClassHierarchy classHierarchy = new ClassHierarchy();

        JavaClass javaClass = new JavaClass(false, "TestClass", "");
         TestMethod testMethod = new TestMethod("shouldUse", "ignore src");
         testMethod.addMethodInvocationStatement(new MethodInvocationStatement("givenSomething"));
         testMethod.addMethodInvocationStatement(new MethodInvocationStatement("thenEnsureSomething"));         
         javaClass.addTestMethod(testMethod);

        classHierarchy.addClass(javaClass);

        int numberOfTestObjects = TestObject.getAllTestObjects(classHierarchy).size();
        assertEquals(1, numberOfTestObjects);
    }
}
