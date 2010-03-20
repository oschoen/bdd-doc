package de.oschoen.bdd.doc.report;

import de.oschoen.bdd.doc.TestObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.assumeThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class BddReportTest {

    private File outDir;

    @Before
    public void createTempOutDir() throws IOException {
        outDir = new File(System.getProperty("java.io.tmpdir"), getClass().getName());
        outDir.mkdir();
    }

    @Test
    public void shouldWriteTestObjectReportToFile() throws IOException {

        BddReport bddReport = new BddReport(outDir, new TestObjectReporter(new ScenarioReporter()) {

            @Override
            public String getReport(TestObject testObject) {
                return testObject.getName();
            }
        });

        bddReport.writeReports(Arrays.asList(new TestObject("testObject1"), new TestObject("testObject2")));

        List<File> writenFiles = Arrays.asList(outDir.listFiles());

        File testObject1Html = new File(outDir, "testObject1.html");
        File testObject2Html = new File(outDir, "testObject2.html");

        assumeThat(writenFiles.size(), is(2));
        assumeThat(writenFiles, hasItem(testObject1Html));
        assumeThat(writenFiles, hasItem(testObject2Html));

        assumeThat(readFileAsString(testObject1Html), is("testObject1"));
        assumeThat(readFileAsString(testObject2Html), is("testObject2"));
    }

    @After
    public void removeTempOutDir() {
        deleteDirectory(outDir);
    }

    static public boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    private static String readFileAsString(File file) throws java.io.IOException {
        byte[] buffer = new byte[(int) file.length()];
        FileInputStream f = new FileInputStream(file);
        f.read(buffer);
        return new String(buffer);
    }
}
