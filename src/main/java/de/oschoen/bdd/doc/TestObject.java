package de.oschoen.bdd.doc;

import java.util.ArrayList;
import java.util.List;


public class TestObject {

    private final String name;
    private final List<Scenario> scenarios = new ArrayList<Scenario>();

    public TestObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }

    @Override
    public String toString() {
        return "TestObject{" +
                "name='" + name + '\'' +
                ", scenarios=" + scenarios +
                '}';
    }

    public static List<TestObject> getAllTestObjects(ClassHierarchy classHierarchy) {

        List<TestObject> testObjects = new ArrayList<TestObject>();
        List<JavaClass> allNoAbstractClasses = classHierarchy.getAllNoAbstractClasses();

        for (JavaClass clazz : allNoAbstractClasses) {
            List<TestMethod> testMethods = classHierarchy.getTestMethodsForClass(clazz.getName());

            if (testMethods.size() > 0) {
                TestObject testObject = new TestObject(clazz.getName());
                for (TestMethod testMethod : testMethods) {
                    testObject.addScenario(testMethod.getScenario());
                }
                testObjects.add(testObject);
            }
        }
       return testObjects;
    }
}
