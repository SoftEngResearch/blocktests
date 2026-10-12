package org.blocktest.types;

public class BlockTestConfig {
    public String methodName;
    public String className;
    public int argumentCount;
    public BlockTestConfig(String className, String methodName, int argumentCount) {
        this.className = className;
        this.methodName = methodName;
        this.argumentCount = argumentCount;
    }

    @Override
    public String toString() {
        return className + "." + methodName + "(" + argumentCount + ")";
    }
}
