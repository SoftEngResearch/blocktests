package org.blocktest.types;

public class StaticMethod {
    public String methodName;
    public String className;
    public int argumentCount;
    public StaticMethod(String className, String methodName, int argumentCount) {
        this.className = className;
        this.methodName = methodName;
        this.argumentCount = argumentCount;
    }

    @Override
    public String toString() {
        return className + "." + methodName + "(" + argumentCount + ")";
    }
}
