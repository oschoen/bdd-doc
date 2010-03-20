package de.oschoen.bdd.doc;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TestMethod {

    private final String name;
    private final String source;
    private final List<MethodInvocationStatement> methodInvocationStatements = new ArrayList<MethodInvocationStatement>();

    public TestMethod(String name, String source) {
        if (!name.startsWith("should")) {
            throw new IllegalStateException("The test method needs to be starts with [should] but [" + name + "] don't do this.");
        }
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void addMethodInvocationStatement(MethodInvocationStatement statement) {
        methodInvocationStatements.add(statement);
    }


    public Scenario getScenario() {


        String scenarioName = transformFromCamelCaseToNaturalLanguage("should", name);

        List<String> givens = new ArrayList<String>();
        List<String> whens = new ArrayList<String>();
        List<String> thens = new ArrayList<String>();

        for (MethodInvocationStatement statement : methodInvocationStatements) {
            if (statement.getMethodSelect().startsWith("given")) {
               givens.add(transformFromMethodInvocationStatementToNaturalLanguage("given", statement));
            }

            if (statement.getMethodSelect().startsWith("when")) {
                whens.add(transformFromMethodInvocationStatementToNaturalLanguage("when", statement));
            }

            if (statement.getMethodSelect().startsWith("then")) { 
                thens.add(transformFromMethodInvocationStatementToNaturalLanguage("then", statement));
            }
        }

        if (givens.size() == 0) {
            throw new IllegalStateException("Test method with name [" + name + "] has no given statement. The method contains following statements [" + methodInvocationStatements + "].");
        }

        if (thens.size() == 0) {
            throw new IllegalStateException("Test method with name [" + name + "] has no then statement. The method contains following statements [" + methodInvocationStatements + "].");
        }

        return new Scenario(scenarioName, givens, whens, thens, source);
    }

    private String transformFromMethodInvocationStatementToNaturalLanguage(String prefixToIgnore, MethodInvocationStatement statement) {

        StringBuilder nlOut = new StringBuilder();
        String methodSelect = statement.getMethodSelect();

        nlOut.append(transformFromCamelCaseToNaturalLanguage(prefixToIgnore, methodSelect));

        if (statement.getArgs().length > 0) {
            nlOut.append(" " + StringUtils.join(statement.getArgs(), " "));
        }

        return nlOut.toString();

    }

    private String transformFromCamelCaseToNaturalLanguage(String prefixToIgnore, String camelCase) {
        return camelCase.substring(prefixToIgnore.length()).replaceAll("([A-Z])", " $1").toLowerCase().substring(1);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestMethod that = (TestMethod) o;

        if (!methodInvocationStatements.equals(that.methodInvocationStatements)) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + methodInvocationStatements.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TestMethod{" +
                "name='" + name + '\'' +
                ", methodInvocationStatements=" + methodInvocationStatements +
                '}';
    }

    public List<MethodInvocationStatement> getMethodInvocationStatements() {
        return methodInvocationStatements;
    }
}
