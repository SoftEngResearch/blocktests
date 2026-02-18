package org.konte.parse;

import org.konte.expression.*;
import static org.konte.lang.Tokens.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import org.konte.expression.Operator;
import org.konte.lang.Language;
import org.konte.lang.Tokens;
import org.konte.model.Model;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.konte.parse.ExpressionParser.*;

public class ExpressionParserBlockTest {

    @Test
    public void testLine179() throws Exception {
        Token t = null;
        int i = 0;
        int cur = 10;
        int[] priority = new int[5];
        Expression[] exps = new Expression[5];
        Token u = null;
        ArrayDeque<Integer> prioStack = new ArrayDeque<Integer>();
        ArrayDeque<Integer> funcStack = new ArrayDeque<Integer>();
        ArrayDeque<Integer> lbracketStack = new ArrayDeque<Integer>();
        if (i > 0) {
            if (exps[i - 1] != null && exps[i - 1] instanceof org.konte.expression.Name) {
                throw new ParseException("Unrecognized function: " + u);
            } else if (!(u instanceof Comparator || u instanceof org.konte.lang.Tokens.Operator || u instanceof org.konte.lang.Tokens.Function || (u == Language.comma && funcStack.size() > 0) || u == Language.left_bracket))
                throw new ParseException("Incorrect expression syntax near " + t);
        }
        prioStack.push(cur);
        lbracketStack.push(i);
        cur += Language.BRACKET_PRIORITY;
        prioStack.push(cur);
        assertTrue(priority[0] == 20);
        assertTrue(lbracketStack.pop() == 0);
        assertTrue(prioStack.pop() == 20);
    }

    @Test
    public void testLine181() throws Exception {
        Token t = null;
        int i = 0;
        int cur = 10;
        int[] priority = new int[5];
        Expression[] exps = new Expression[5];
        Token u = null;
        ArrayDeque<Integer> prioStack = new ArrayDeque<Integer>();
        ArrayDeque<Integer> funcStack = new ArrayDeque<Integer>();
        ArrayDeque<Integer> lbracketStack = new ArrayDeque<Integer>();
        if (i > 0) {
            if (exps[i - 1] != null && exps[i - 1] instanceof org.konte.expression.Name) {
                throw new ParseException("Unrecognized function: " + u);
            } else if (!(u instanceof Comparator || u instanceof org.konte.lang.Tokens.Operator || u instanceof org.konte.lang.Tokens.Function || (u == Language.comma && funcStack.size() > 0) || u == Language.left_bracket))
                throw new ParseException("Incorrect expression syntax near " + t);
        }
        prioStack.push(cur);
        lbracketStack.push(i);
        cur += Language.BRACKET_PRIORITY;
        prioStack.push(cur);
        assertTrue(prioStack.pop() + prioStack.pop() == 30);
    }

    @Test(expected = ParseException.class)
    public void testLine183() throws Exception {
        Token t = null;
        int i = 1;
        int cur = 10;
        int[] priority = new int[5];
        Expression[] exps = new Expression[5];
        Token u = null;
        ArrayDeque<Integer> prioStack = new ArrayDeque<Integer>();
        ArrayDeque<Integer> funcStack = new ArrayDeque<Integer>();
        ArrayDeque<Integer> lbracketStack = new ArrayDeque<Integer>();
        if (i > 0) {
            if (exps != null && true) {
                throw new ParseException("Unrecognized function: " + u);
            } else if (!(u instanceof Comparator || u instanceof org.konte.lang.Tokens.Operator || u instanceof org.konte.lang.Tokens.Function || (u == Language.comma && funcStack.size() > 0) || u == Language.left_bracket))
                throw new ParseException("Incorrect expression syntax near " + t);
        }
        prioStack.push(cur);
        lbracketStack.push(i);
        cur += Language.BRACKET_PRIORITY;
        prioStack.push(cur);
    }

    @Test(expected = ParseException.class)
    public void testLine185() throws Exception {
        Token t = null;
        int i = 1;
        int cur = 10;
        int[] priority = new int[5];
        Expression[] exps = new Expression[5];
        Token u = Language.comma;
        ArrayDeque<Integer> prioStack = new ArrayDeque<Integer>();
        ArrayDeque<Integer> funcStack = new ArrayDeque<Integer>();
        ArrayDeque<Integer> lbracketStack = new ArrayDeque<Integer>();
        if (i > 0) {
            if (exps[i - 1] != null && true) {
                throw new ParseException("Unrecognized function: " + u);
            } else if (!(u instanceof Comparator || u instanceof org.konte.lang.Tokens.Operator || u instanceof org.konte.lang.Tokens.Function || (u == Language.comma && funcStack.size() > 0) || u == Language.left_bracket))
                throw new ParseException("Incorrect expression syntax near " + t);
        }
        prioStack.push(cur);
        lbracketStack.push(i);
        cur += Language.BRACKET_PRIORITY;
        prioStack.push(cur);
    }
}
