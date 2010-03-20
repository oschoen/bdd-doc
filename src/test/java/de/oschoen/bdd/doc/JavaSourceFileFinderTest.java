package de.oschoen.bdd.doc;

import de.oschoen.bdd.doc.test.TestDirectory;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class JavaSourceFileFinderTest {

    private TestDirectory testDirectory = new TestDirectory("javaSourceFileFinderTestDirectories");

    @Test
    public void shouldFindThreeJavaSourceFilesinDeepDirectoryStructure() {
        JavaSourceFileFinder fileFinder = new JavaSourceFileFinder(testDirectory.getTestFile("deepDirectoryStructureWithThreeJavaSourceFiles"));

        Collection<File> javaFiles = fileFinder.findJavaSourceFiles();

        assertThat(javaFiles.size(), is(3));
        assertThat(javaFiles, hasItem(testDirectory.getTestFile("deepDirectoryStructureWithThreeJavaSourceFiles/TestClass1Test.java")));
        assertThat(javaFiles, hasItem(testDirectory.getTestFile("deepDirectoryStructureWithThreeJavaSourceFiles/dir1/dir2/TestClass2Test.java")));
        assertThat(javaFiles, hasItem(testDirectory.getTestFile("deepDirectoryStructureWithThreeJavaSourceFiles/dir1/dir3/TestClass2Test.java")));
    }

    @Test
    public void shouldFindTheTwoJavaSourceFiles() {
        JavaSourceFileFinder fileFinder = new JavaSourceFileFinder(testDirectory.getTestFile("directoryWithTwoJavaSourceFiles"));

        Collection<File> javaFiles = fileFinder.findJavaSourceFiles();

        assertThat(javaFiles.size(), is(2));
        assertThat(javaFiles, hasItem(testDirectory.getTestFile("directoryWithTwoJavaSourceFiles/TestClass1Test.java")));
        assertThat(javaFiles, hasItem(testDirectory.getTestFile("directoryWithTwoJavaSourceFiles/TestClass2Test.java")));
    }

    @Test
    public void shouldIgnoreTheHiddenDirectory() {
        JavaSourceFileFinder fileFinder = new JavaSourceFileFinder(testDirectory.getTestFile("directoryWithHiddenDirectory"));

        Collection<File> javaFiles = fileFinder.findJavaSourceFiles();

        assertThat(javaFiles.size(), is(0));
    }


    @Test
    public void shouldOnlyFindJavaSourceFileNotDirectories() {
        JavaSourceFileFinder fileFinder = new JavaSourceFileFinder(testDirectory.getTestFile("directoryWithJavaInName"));

        Collection<File> javaFiles = fileFinder.findJavaSourceFiles();

        assertThat(javaFiles.size(), is(1));
        assertThat(javaFiles, hasItem(testDirectory.getTestFile("directoryWithJavaInName/test.java/TestClassTest.java")));
    }

    @Test
    public void shouldOnlyFindTheJavaSourceFile() {
        JavaSourceFileFinder fileFinder = new JavaSourceFileFinder(testDirectory.getTestFile("directoryWithNonJavaSourceFiles"));

        Collection<File> javaFiles = fileFinder.findJavaSourceFiles();

        assertThat(javaFiles.size(), is(1));
        assertThat(javaFiles, hasItem(testDirectory.getTestFile("directoryWithNonJavaSourceFiles/TestClassTest.java")));
    }

   

}
