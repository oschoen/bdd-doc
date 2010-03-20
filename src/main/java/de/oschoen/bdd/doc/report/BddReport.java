package de.oschoen.bdd.doc.report;

import de.oschoen.bdd.doc.TestObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class BddReport {

    private final File rootDirectory;
    private final TestObjectReporter testObjectReporter;

    public BddReport(File rootDirectory, TestObjectReporter testObjectReporter) {
        assert rootDirectory.isDirectory();
        this.rootDirectory = rootDirectory;
        this.testObjectReporter = testObjectReporter;        
    }
    
    public void writeReports(Collection<TestObject> testObjects) {
        for (TestObject testObject : testObjects) {
            try {
                FileWriter writer = new FileWriter(new File(rootDirectory, testObject.getName() + ".html"));
                writer.append(testObjectReporter.getReport(testObject));
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException("Cant generate report for test object [" + testObject + "].");
            }

        }
    }
}
