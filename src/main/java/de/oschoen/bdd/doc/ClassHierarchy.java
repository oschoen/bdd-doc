package de.oschoen.bdd.doc;

import java.util.*;


public class ClassHierarchy {

    private final Map<String, JavaClass> classes = new HashMap<String, JavaClass>();

    public void addClass(JavaClass clazz) {
        if (classes.containsKey(clazz.getName())) {
             throw new IllegalStateException("There is already a class with name [" + clazz.getName() + "]  registered.");
        } else {
            classes.put(clazz.getName(), clazz);
        }
    }

    public JavaClass getClass(String name) {
        return classes.get(name);
    }

    public List<TestMethod> getTestMethodsForClass(String className){
       Set<String> existingTestMethods = new HashSet<String>();
       List<TestMethod> testMethods = new ArrayList<TestMethod>();
        
        for (JavaClass theClass = classes.get(className); theClass!= null; theClass = classes.get(theClass.getExtendsFrom())) {

            for (TestMethod testMethod : theClass.getTestMethods()) {
                if (!existingTestMethods.contains(testMethod.getName())) {
                    testMethods.add(testMethod);
                    existingTestMethods.add(testMethod.getName());
                }
            }

        }

        return testMethods;
    }

    public List<JavaClass> getAllNoAbstractClasses() {
         List<JavaClass> noAbstractClasses = new ArrayList<JavaClass>();

        for (JavaClass javaClass : classes.values()) {
            if (javaClass.isNotAbstractClass()) {
                noAbstractClasses.add(javaClass);
            }
        }

        return noAbstractClasses;
    }
}
