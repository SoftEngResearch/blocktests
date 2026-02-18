package org.blocktest.types;

public enum Flow {
    IfStmt, Then, Else, ElseIf;

    public static Flow IfStmt() {
        return IfStmt;
    }

    public static Flow Then() {
        return Then;
    }

    public static Flow Else() {
        return Else;
    }

    public static Flow ElseIf() {
        return ElseIf;
    }
}
