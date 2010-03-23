package de.oschoen.bdd.doc;

import com.sun.source.tree.*;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import org.junit.Test;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;

public class BddTreePathScanner extends TreePathScanner<Void, Void> {

    private String currentPackageName;
    private JavaClass currentClass;
    private TestMethod currentTestMethod;
    private final Trees trees;
    private final ClassHierarchy classHierarchy;

    public BddTreePathScanner(Trees trees, ClassHierarchy classHierarchy) {
        this.trees = trees;
        this.classHierarchy = classHierarchy;
    }

    

    @Override
    public Void visitClass(ClassTree node, Void aVoid) {

        // Get the current path of the node
        TreePath path = getCurrentPath();

        //Get the type element corresponding to the class
        TypeElement e = (TypeElement) trees.getElement(path);

        if (e != null) {
            boolean isAbstract = node.getModifiers().getFlags().contains(Modifier.ABSTRACT);
            String simpleName = e.getSimpleName().toString();
            String extendsFrom = null;
            if (node.getExtendsClause() != null) {
                extendsFrom = e.getSuperclass().toString();
            }
            currentClass = new JavaClass(isAbstract, path.getCompilationUnit().getPackageName().toString(), simpleName, extendsFrom);
            classHierarchy.addClass(currentClass);
            return super.visitClass(node, aVoid);
        } else {
            return aVoid;
        }
    }


    @Override
    public Void visitMethod(MethodTree node, Void aVoid) {
        if (node.getModifiers().getAnnotations().size() > 0) {
            List<? extends AnnotationTree> annotations = node.getModifiers().getAnnotations();

            for (AnnotationTree annotation : annotations) {
                if (annotation.getAnnotationType().toString().equals(Test.class.getSimpleName())) {

                    if (node.getParameters().size() == 0) {
                        currentTestMethod = new TestMethod(node.getName().toString(), node.getBody().toString());
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
}
