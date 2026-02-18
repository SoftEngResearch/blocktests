package org.blocktest.utils;

import java.util.Arrays;
import java.util.List;

import com.github.javaparser.ast.expr.AssignExpr;

public class Constant {
    public final static List<AssignExpr.Operator> COMPOUND_ASSIGN_OPERATORS = Arrays.asList(AssignExpr.Operator.PLUS,
            AssignExpr.Operator.MINUS,
            AssignExpr.Operator.MULTIPLY, AssignExpr.Operator.DIVIDE, AssignExpr.Operator.BINARY_AND,
            AssignExpr.Operator.BINARY_OR, AssignExpr.Operator.XOR, AssignExpr.Operator.REMAINDER,
            AssignExpr.Operator.LEFT_SHIFT, AssignExpr.Operator.SIGNED_RIGHT_SHIFT,
            AssignExpr.Operator.UNSIGNED_RIGHT_SHIFT);

    public enum StatementType {
        GIVEN, TARGET, ASSERTION
    }

    public final static String DECLARE_NAME = "blocktest";
    public final static String DECLARE_NAME_LAMBDA = "lambdatest";
    public final static String CHECK_FLOW = "checkFlow";
    public final static String CHECK_EQ = "checkEq";
    public final static String CHECK_TRUE = "checkTrue";
    public final static String CHECK_FALSE = "checkFalse";
    public final static String CHECK_RETURN_EQ = "checkReturnEq";
    public final static String CHECK_RETURN_TRUE = "checkReturnTrue";
    public final static String CHECK_RETURN_FALSE = "checkReturnFalse";
    public final static String CHECK_EXP = "expect";
    public final static String GIVEN = "given";
    public final static String ARGS = "args";
    public final static String ARGS_VAR = "argsVar";
    public final static String END = "end";
    public final static String START = "start";
    public final static String DELAY = "delay";
    public final static String SETUP = "setup";
    public final static String NO_INIT = "noInit";
    public final static String MOCK = "mock";
    public final static String MOCK_STR = "mockString";

    // control blocktest().end()
    public final static String LAMBDA = "LAMBDA";
    public final static String LAMBDA_RETURN = "lambdaReturn";
    public final static String METHOD_RETURN = "methodReturn";
    public final static Object lambdaReturn = new Object();
    public final static Object methodReturn = new Object();
    public final static List<String> PRIMITIVE_TYPES = Arrays.asList("int", "long", "double", "float", "boolean", "char",
            "byte", "short",
            "String", "java.lang.String", "int[]", "long[]", "double[]", "float[]", "boolean[]", "char[]", "byte[]",
            "short[]",
            "String[]", "java.lang.String[]");
}
