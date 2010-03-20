package de.oschoen.bdd.doc;

import com.sun.source.tree.*;
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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.StringWriter;
import java.util.*;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("*")
public class BddProcessor extends AbstractProcessor {

    private ClassHierarchy classHierarchy = new ClassHierarchy();


    public ClassHierarchy parseFiles(Collection<File> files) {
        //Get an instance of java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        //Get a new instance of the standard file manager implementation
        StandardJavaFileManager fileManager = compiler.
                getStandardFileManager(null, null, null);


        Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromFiles(files);

        // Create the compilation task
        StringWriter errors = new StringWriter();
        JavaCompiler.CompilationTask task = compiler.getTask(errors, fileManager, null,
                null, null, compilationUnits);

        // Create a list to hold annotation processors
        LinkedList<AbstractProcessor> processors = new LinkedList<AbstractProcessor>();

        processors.add(this);

        task.setProcessors(processors);

        // Perform the compilation task.
        if (!task.call()) {
            System.err.println("Compilation errors: " + errors.toString());
        }

        return classHierarchy;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnvironment) {
        for (Element e : roundEnvironment.getRootElements()) {
            final Trees trees = Trees.instance(processingEnv);
            final TreePath root = trees.getPath(e);
            new TreePathScanner<Void, Void>() {

                private JavaClass currentClass;
                private TestMethod currentTestMethod;

                @Override
                public Void visitClass(ClassTree node, Void aVoid) {

                    // Get the current path of the node
                    TreePath path = getCurrentPath();

                    //Get the type element corresponding to the class
                    TypeElement e = (TypeElement) trees.getElement(path);

                    boolean isAbstract = node.getModifiers().getFlags().contains(Modifier.ABSTRACT);
                    String name = e.getQualifiedName().toString();
                    String extendsFrom = null;
                    if (node.getExtendsClause() != null) {
                        extendsFrom = e.getSuperclass().toString();
                    }
                    currentClass = new JavaClass(isAbstract, name, extendsFrom);
                    classHierarchy.addClass(currentClass);
                    return super.visitClass(node, aVoid);
                }


                @Override
                public Void visitMethod(MethodTree node, Void aVoid) {
                    if (node.getModifiers().getAnnotations().size() > 0) {
                        List<? extends AnnotationTree> annotations = node.getModifiers().getAnnotations();

                        for (AnnotationTree annotation : annotations) {
                            if (annotation.getAnnotationType().toString().equals(Test.class.getSimpleName())) {

                                if (node.getParameters().size() == 0) {
                                    currentTestMethod = new TestMethod(node.getName().toString());
                                    currentClass.addTestMethod(currentTestMethod);
                                    return super.visitMethod(node, aVoid);
                                }

                            }
                        }

                    }
                    return aVoid;
                }

                public
                @Override
                Void visitMethodInvocation(MethodInvocationTree node, Void p) {

                    if (currentTestMethod != null) {
                        List<String> argsAsString = new ArrayList<String>();
                        for (ExpressionTree args : node.getArguments()) {
                            argsAsString.add(args.toString());
                        }
                        currentTestMethod.addMethodInvocationStatement(new MethodInvocationStatement(node.getMethodSelect().toString(), argsAsString.toArray(new String[argsAsString.size()])));
                    }
                    return super.visitMethodInvocation(node, p);
                }
            }.scan(root, null);
        }
        return true;
    }


}
