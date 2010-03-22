package de.oschoen.bdd.doc;

import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import de.oschoen.bdd.doc.report.BddReport;
import de.oschoen.bdd.doc.report.ScenarioReporter;
import de.oschoen.bdd.doc.report.TestObjectReporter;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.util.List;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("*")
@SupportedOptions(BddProcessor.REPORT_OUT_DIR_OPTION_NAME)
public class BddProcessor extends AbstractProcessor {

    private ClassHierarchy classHierarchy = new ClassHierarchy();
    public static final String REPORT_OUT_DIR_OPTION_NAME = "reportoutdir";

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnvironment) {

        String reportOutputDir = processingEnv.getOptions().get(REPORT_OUT_DIR_OPTION_NAME);

        if (reportOutputDir != null) {
            Trees trees = Trees.instance(processingEnv);
            TreePathScanner<Void, Void> bddTreePathScanner = new BddTreePathScanner(trees, classHierarchy);

            for (Element e : roundEnvironment.getRootElements()) {
                TreePath root = trees.getPath(e);
                bddTreePathScanner.scan(root, null);
            }

            List<TestObject> testObjects = TestObject.getAllTestObjects(classHierarchy);

            new BddReport(new File(processingEnv.getOptions().get(REPORT_OUT_DIR_OPTION_NAME)), new TestObjectReporter(new ScenarioReporter())).writeReports(testObjects);

        }
        return true;
    }


}
