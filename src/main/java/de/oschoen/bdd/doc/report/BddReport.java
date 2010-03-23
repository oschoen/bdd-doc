package de.oschoen.bdd.doc.report;

import de.oschoen.bdd.doc.TestObject;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.Collection;

public class BddReport {

    private final Filer filer;
    private final TestObjectReporter testObjectReporter;

    public BddReport(Filer filer, TestObjectReporter testObjectReporter) {
        this.filer = filer;
        this.testObjectReporter = testObjectReporter;        
    }
    
    public void writeReports(Collection<TestObject> testObjects) {
        for (TestObject testObject : testObjects) {
            try {
                FileObject file = filer.createResource(StandardLocation.SOURCE_OUTPUT, testObject.getPackageName(), testObject.getSimpleName() + ".html", (Element) null);

                OutputStream os = file.openOutputStream();
                PrintWriter pw = new PrintWriter( os );
                pw.append(testObjectReporter.getReport(testObject));
                pw.close();
            } catch (IOException e) {
                throw new RuntimeException("Cant generate report for test object [" + testObject + "].", e);
            }

        }
    }
}
