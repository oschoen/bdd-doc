package de.oschoen.bdd.doc;

import java.util.*;


public class JavaClass {

    private final boolean abstractClass;

    private final String packageName;
    private final String simpleName;
    private final String extendsFrom;
    private final List<TestMethod> testMethods = new ArrayList<TestMethod>();

    public JavaClass(boolean abstractClass, String packageName, String simpleName, String extendsFrom) {
        this.abstractClass = abstractClass;
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.extendsFrom = extendsFrom;
    }

    public boolean isNotAbstractClass() {
        return !abstractClass;
    }


    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }
    
    public String getName() {
        return packageName + "." + simpleName;
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
        if (!getName().equals(javaClass.getName())) return false;
        return testMethods.equals(javaClass.testMethods);

        }

    @Override
    public int hashCode() {
        int result = (abstractClass ? 1 : 0);
        result = 31 * result + getName().hashCode();
        result = 31 * result + extendsFrom.hashCode();
        result = 31 * result + testMethods.hashCode();
        return result;
    }
}
