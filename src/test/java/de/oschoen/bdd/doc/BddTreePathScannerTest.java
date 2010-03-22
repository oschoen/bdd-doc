package de.oschoen.bdd.doc;

import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import org.junit.Test;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class BddTreePathScannerTest {

    private TestDirectory testDirectory = new TestDirectory("testClasses");


    @Test
    public void shouldFindAllClasses() {

        ClassHierarchy classHierarchy = processClasses(testDirectory.getTestFiles("ExampleTestClazz.java", "BaseExampleTestClazz.java"));

        assertEquals("testClasses.BaseExampleTestClazz", classHierarchy.getClass("testClasses.BaseExampleTestClazz").getName());
        assertEquals("testClasses.ExampleTestClazz", classHierarchy.getClass("testClasses.ExampleTestClazz").getName());


    }

    @Test
     public void shouldOnlyFindTestMethods() {

         ClassHierarchy classHierarchy = processClasses(testDirectory.getTestFiles("ExampleTestClazz.java", "BaseExampleTestClazz.java"));

         Collection<TestMethod> testMethods = classHierarchy.getClass("testClasses.ExampleTestClazz").getTestMethods();

         assertEquals(1, testMethods.size());

         thenEnsureTestMethodWithName(testMethods, "shouldBeAnotherExample");

     }

     @Test
     public void shouldOnlyFindMethodInvocationsStatements() {

         ClassHierarchy classHierarchy = processClasses(testDirectory.getTestFiles("ExampleTestClazz.java", "BaseExampleTestClazz.java"));

         Collection<TestMethod> testMethods = classHierarchy.getClass("testClasses.ExampleTestClazz").getTestMethods();

         assertEquals(1, testMethods.size());

         thenEnsureTestMethodWithName(testMethods, "shouldBeAnotherExample");

     }

     @Test
     public void shouldIgnoreTestMethodsWithParameters() {
         ClassHierarchy classHierarchy = processClasses(testDirectory.getTestFiles("ExampleTestClazzWithMethodParam.java"));
         Collection<TestMethod> testMethods = classHierarchy.getClass("testClasses.ExampleTestClazzWithMethodParam").getTestMethods();

         assertEquals(1, testMethods.size());
         thenEnsureTestMethodWithName(testMethods, "shouldWork");

     }

      @Test
     public void shouldIgnoreAnonymousClasses() {
         ClassHierarchy classHierarchy = processClasses(testDirectory.getTestFiles("ExampleTestClazzWithAnonymousClass.java"));
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


    private ClassHierarchy processClasses(Collection<File> classes) {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        StandardJavaFileManager fileManager = compiler.
                getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromFiles(classes);

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null,
                null, null, compilationUnits);

        LinkedList<AbstractProcessor> processors = new LinkedList<AbstractProcessor>();

        ClassHierarchy classHierarchy = new ClassHierarchy();
        processors.add(new Processor(classHierarchy));

        task.setProcessors(processors);

        task.call();
        
        return classHierarchy;
    }

    @SupportedSourceVersion(SourceVersion.RELEASE_6)
    @SupportedAnnotationTypes("*")
    private static class Processor extends AbstractProcessor {

        private final ClassHierarchy classHierarchy;

        public Processor(ClassHierarchy classHierarchy) {
            this.classHierarchy = classHierarchy;
        }

        @Override
        public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
            Trees trees = Trees.instance(processingEnv);
            TreePathScanner<Void, Void> bddTreePathScanner = new BddTreePathScanner(trees, classHierarchy);

            for (Element e : roundEnv.getRootElements()) {
                TreePath root = trees.getPath(e);
                bddTreePathScanner.scan(root, null);
            }
            return true;
        }
    }


    private static class TestDirectory {

        private final String rootDirName;

        public TestDirectory(String rootDirName) {
            this.rootDirName = rootDirName;
        }

        public Collection<File> getTestFiles(String... fileNames) {
            Collection<File> testFiles = new ArrayList<File>();
            for (String fileName : fileNames) {
                testFiles.add(getTestFile(fileName));
            }
            return testFiles;
        }

        public File getTestFile(String fileName) {
            URL resource = getClass().getResource("/" + rootDirName + "/" + fileName);

            if (resource == null) {
                fail("Cant find test file [/" + rootDirName + "/" + fileName + "].");
            }
            try {
                return new File(resource.toURI());
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid uri for file [" + fileName + "]", e);
            }
        }
    }
}
