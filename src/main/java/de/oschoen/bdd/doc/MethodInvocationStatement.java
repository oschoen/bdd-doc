package de.oschoen.bdd.doc;

import java.util.Arrays;

public class MethodInvocationStatement {


    private final String methodSelect;
    private final String[] args;

    public MethodInvocationStatement(String methodSelect) {
        this(methodSelect, new String[0]);
    }

    public MethodInvocationStatement(String methodSelect, String[] args) {
        assert (args != null);

        this.args = args;
        this.methodSelect = methodSelect;
    }

    public String getMethodSelect() {
        return methodSelect;
    }

    public String[] getArgs() {
        return args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodInvocationStatement that = (MethodInvocationStatement) o;

        if (!Arrays.equals(args, that.args)) return false;
        return methodSelect.equals(that.methodSelect);

    }

    @Override
    public int hashCode() {
        int result = methodSelect.hashCode();
        result = 31 * result + Arrays.hashCode(args);
        return result;
    }

    @Override
    public String toString() {
        return "MethodInvocationStatement{" +
                "methodSelect='" + methodSelect + '\'' +
                ", args=" + (args == null ? null : Arrays.asList(args)) +
                '}';
    }
}
