package de.oschoen.bdd.doc;

import de.oschoen.bdd.doc.report.BddReport;
import de.oschoen.bdd.doc.report.ScenarioReporter;
import de.oschoen.bdd.doc.report.TestObjectReporter;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String... args) {

        if (args.length != 2) {
            throw new IllegalArgumentException("Please pass source and report output directory as arguments.");
        }

        JavaSourceFileFinder root = new JavaSourceFileFinder(new File(args[0]));

        BddProcessor bddProcessor = new BddProcessor();

        ClassHierarchy classHierarchy = bddProcessor.parseFiles(root.findJavaSourceFiles());

        List<TestObject> testObjects = TestObject.getAllTestObjects(classHierarchy);

        new BddReport(new File(args[1]), new TestObjectReporter(new ScenarioReporter())).writeReports(testObjects);

    }
}
