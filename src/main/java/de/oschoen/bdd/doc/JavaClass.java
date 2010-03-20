package de.oschoen.bdd.doc;

import java.util.*;


public class JavaClass {

    private final boolean abstractClass;
    private final String name;    
    private final String extendsFrom;
    private final List<TestMethod> testMethods = new ArrayList<TestMethod>();

    public JavaClass(boolean abstractClass, String name, String extendsFrom) {
        this.abstractClass = abstractClass;
        this.name = name;
        this.extendsFrom = extendsFrom;
    }

    public boolean isNotAbstractClass() {
        return !abstractClass;
    }

    public String getName() {
        return name;
    }

    public String getExtendsFrom() {
        return extendsFrom;
    }

    public void addTestMethod(TestMethod testMethod) {
        testMethods.add(testMethod);
    }

    public List<TestMethod> getTestMethods() {
        return testMethods;    
    }

    public boolean hasTestMethods() {
        return testMethods.size() > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaClass javaClass = (JavaClass) o;

        if (abstractClass != javaClass.abstractClass) return false;
        if (!extendsFrom.equals(javaClass.extendsFrom)) return false;
        if (!name.equals(javaClass.name)) return false;
        return testMethods.equals(javaClass.testMethods);

        }

    @Override
    public int hashCode() {
        int result = (abstractClass ? 1 : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + extendsFrom.hashCode();
        result = 31 * result + testMethods.hashCode();
        return result;
    }
}
