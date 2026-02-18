package co.streamx.fluent.extree.expression;

import java.io.Serializable;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import co.streamx.fluent.extree.expression.ExpressionStack.BranchExpression;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static co.streamx.fluent.extree.expression.ExpressionMethodVisitor.*;

public class ExpressionMethodVisitorBlockTest {

    @Test
    public void testDouble() throws Exception {
        Expression first = null;
        Expression e = null;
        int opcode = Opcodes.F2D;
        ExpressionStack _exprStack;
        _exprStack = new ExpressionStack();
        _exprStack.push(Expression.constant(1f, Float.TYPE));
        int f2f = opcode == Opcodes.F2D ? 1 : 0;
        first = _exprStack.pop();
        e = Expression.convert(first, ExpressionMethodVisitor.NumericTypeLookup[opcode % Opcodes.F2I + f2f]);
        assertEquals("(double)1.0", e.toString());
        assertTrue(e != null);
    }

    @Test
    public void testLong() throws Exception {
        Expression first = null;
        Expression e = null;
        int opcode = Opcodes.F2L;
        ExpressionStack _exprStack;
        _exprStack = new ExpressionStack();
        _exprStack.push(Expression.constant(1f, Float.TYPE));
        int f2f = opcode == Opcodes.F2D ? 1 : 0;
        first = _exprStack.pop();
        e = Expression.convert(first, ExpressionMethodVisitor.NumericTypeLookup[opcode % Opcodes.F2I + f2f]);
        assertEquals("(long)1.0", e.toString());
        assertTrue(e != null);
    }

    @Test
    public void testInt() throws Exception {
        Expression first = null;
        Expression e = null;
        int opcode = Opcodes.F2I;
        ExpressionStack _exprStack;
        _exprStack = new ExpressionStack();
        _exprStack.push(Expression.constant(1f, Float.TYPE));
        int f2f = opcode == Opcodes.F2D ? 1 : 0;
        first = _exprStack.pop();
        e = Expression.convert(first, ExpressionMethodVisitor.NumericTypeLookup[opcode % Opcodes.F2I + f2f]);
        assertEquals("(int)1.0", e.toString());
        assertTrue(e != null);
    }
}
