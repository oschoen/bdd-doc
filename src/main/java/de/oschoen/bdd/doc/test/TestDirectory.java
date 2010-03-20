package de.oschoen.bdd.doc.test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.fail;

public class TestDirectory {

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
