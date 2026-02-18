package org.mvel2.optimizers.impl.asm;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.mvel2.CompileException;
import org.mvel2.DataConversion;
import org.mvel2.MVEL;
import org.mvel2.OptimizationFailure;
import org.mvel2.ParserContext;
import org.mvel2.PropertyAccessException;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.mvel2.ast.FunctionInstance;
import org.mvel2.ast.TypeDescriptor;
import org.mvel2.ast.WithNode;
import org.mvel2.compiler.Accessor;
import org.mvel2.compiler.AccessorNode;
import org.mvel2.compiler.ExecutableAccessor;
import org.mvel2.compiler.ExecutableLiteral;
import org.mvel2.compiler.ExecutableStatement;
import org.mvel2.compiler.PropertyVerifier;
import org.mvel2.integration.GlobalListenerFactory;
import org.mvel2.integration.PropertyHandler;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.optimizers.AbstractOptimizer;
import org.mvel2.optimizers.AccessorOptimizer;
import org.mvel2.optimizers.OptimizationNotSupported;
import org.mvel2.optimizers.impl.refl.nodes.Union;
import org.mvel2.util.JITClassLoader;
import org.mvel2.util.MVELClassLoader;
import org.mvel2.util.MethodStub;
import org.mvel2.util.NullType;
import org.mvel2.util.ParseTools;
import org.mvel2.util.PropertyTools;
import org.mvel2.util.StringAppender;
import static java.lang.String.valueOf;
import static java.lang.System.getProperty;
import static java.lang.Thread.currentThread;
import static java.lang.reflect.Array.getLength;
import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.STATIC;
import static org.mvel2.DataConversion.canConvert;
import static org.mvel2.DataConversion.convert;
import static org.mvel2.MVEL.eval;
import static org.mvel2.MVEL.isAdvancedDebugging;
import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.AASTORE;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ARRAYLENGTH;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.BALOAD;
import static org.objectweb.asm.Opcodes.BASTORE;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.CALOAD;
import static org.objectweb.asm.Opcodes.CASTORE;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DALOAD;
import static org.objectweb.asm.Opcodes.DASTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.DUP_X1;
import static org.objectweb.asm.Opcodes.DUP_X2;
import static org.objectweb.asm.Opcodes.FALOAD;
import static org.objectweb.asm.Opcodes.FASTORE;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IALOAD;
import static org.objectweb.asm.Opcodes.IASTORE;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.ICONST_3;
import static org.objectweb.asm.Opcodes.ICONST_4;
import static org.objectweb.asm.Opcodes.ICONST_5;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.IF_ICMPLT;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.LALOAD;
import static org.objectweb.asm.Opcodes.LASTORE;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.NEWARRAY;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.SALOAD;
import static org.objectweb.asm.Opcodes.SASTORE;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Opcodes.SWAP;
import static org.objectweb.asm.Type.getConstructorDescriptor;
import static org.objectweb.asm.Type.getDescriptor;
import static org.objectweb.asm.Type.getInternalName;
import static org.objectweb.asm.Type.getMethodDescriptor;
import static org.objectweb.asm.Type.getType;
import static org.mvel2.ast.TypeDescriptor.getClassReference;
import static org.mvel2.integration.GlobalListenerFactory.hasGetListeners;
import static org.mvel2.integration.GlobalListenerFactory.notifyGetListeners;
import static org.mvel2.integration.PropertyHandlerFactory.getNullMethodHandler;
import static org.mvel2.integration.PropertyHandlerFactory.getNullPropertyHandler;
import static org.mvel2.integration.PropertyHandlerFactory.getPropertyHandler;
import static org.mvel2.integration.PropertyHandlerFactory.hasNullMethodHandler;
import static org.mvel2.integration.PropertyHandlerFactory.hasNullPropertyHandler;
import static org.mvel2.integration.PropertyHandlerFactory.hasPropertyHandler;
import static org.mvel2.util.ArrayTools.findFirst;
import static org.mvel2.util.ParseTools.EMPTY_OBJ_ARR;
import static org.mvel2.util.ParseTools.balancedCapture;
import static org.mvel2.util.ParseTools.balancedCaptureWithLineAccounting;
import static org.mvel2.util.ParseTools.captureContructorAndResidual;
import static org.mvel2.util.ParseTools.determineActualTargetMethod;
import static org.mvel2.util.ParseTools.findClass;
import static org.mvel2.util.ParseTools.getBaseComponentType;
import static org.mvel2.util.ParseTools.getBestCandidate;
import static org.mvel2.util.ParseTools.getBestConstructorCandidate;
import static org.mvel2.util.ParseTools.getSubComponentType;
import static org.mvel2.util.ParseTools.getWidenedTarget;
import static org.mvel2.util.ParseTools.isPrimitiveWrapper;
import static org.mvel2.util.ParseTools.parseMethodOrConstructor;
import static org.mvel2.util.ParseTools.parseParameterList;
import static org.mvel2.util.ParseTools.repeatChar;
import static org.mvel2.util.ParseTools.subCompileExpression;
import static org.mvel2.util.ParseTools.subset;
import static org.mvel2.util.PropertyTools.getFieldOrAccessor;
import static org.mvel2.util.PropertyTools.getFieldOrWriteAccessor;
import static org.mvel2.util.ReflectionUtil.toNonPrimitiveArray;
import static org.mvel2.util.ReflectionUtil.toNonPrimitiveType;
import static org.mvel2.util.Varargs.normalizeArgsForVarArgs;
import static org.mvel2.util.Varargs.paramTypeVarArgsSafe;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mvel2.optimizers.impl.asm.ASMAccessorOptimizer.*;

public class ASMAccessorOptimizerBlockTest {

    @Test(expected = CompileException.class)
    public void testFirst() throws Exception {
        Member member = org.mvel2.DataConversion.class.getDeclaredField("CONVERTERS");
        String property = "ArrayTypeMarker";
        char[] expr = new char[] {};
        int tkStart = 0;
        try {
            Class c = Class.forName(member.getDeclaringClass().getName() + "$" + property);
            throw new CompileException("name collision between innerclass: " + c.getCanonicalName() + "; and bean accessor: " + property + " (" + member.toString() + ")", expr, tkStart);
        } catch (ClassNotFoundException e2) {
            // fallthru
        }
    }

    @Test
    public void testSecond() throws Exception {
        Member member = org.mvel2.DataConversion.class.getDeclaredField("CONVERTERS");
        String property = "ArrayTypeMarkers";
        char[] expr = new char[] {};
        int tkStart = 0;
        try {
            Class c = Class.forName(member.getDeclaringClass().getName() + "$" + property);
            throw new CompileException("name collision between innerclass: " + c.getCanonicalName() + "; and bean accessor: " + property + " (" + member.toString() + ")", expr, tkStart);
        } catch (ClassNotFoundException e2) {
            // fallthru
        }
    }

    @Test
    public void fallthru() throws Exception {
        Member member = ASMAccessorOptimizer.class.getMethod("isLiteralOnly");
        String property = "isLiteralOnly";
        char[] expr = new char[] {};
        int tkStart = 0;
        try {
            Class c = Class.forName(member.getDeclaringClass().getName() + "$" + property);
            throw new CompileException("name collision between innerclass: " + c.getCanonicalName() + "; and bean accessor: " + property + " (" + member.toString() + ")", expr, tkStart);
        } catch (ClassNotFoundException e2) {
            // fallthru
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void NSM() throws Exception {
        Member member = ASMAccessorOptimizer.class.getMethod("isLiteralOnlyX");
        String property = "isLiteralOnlyX";
        char[] expr = new char[] {};
        int tkStart = 0;
        try {
            Class c = Class.forName(member.getDeclaringClass().getName() + "$" + property);
            throw new CompileException("name collision between innerclass: " + c.getCanonicalName() + "; and bean accessor: " + property + " (" + member.toString() + ")", expr, tkStart);
        } catch (ClassNotFoundException e2) {
            // fallthru
        }
    }

    @Test
    public void testLine1847() throws Exception {
        ExecutableStatement[] es = null;
        Object[] args = new Object[] { null, null, Double.valueOf("5"), new Object() };
        Class[] argTypes = new Class[] { null, Float.class, Integer.class, Float.class };
        for (int i = 0; i < args.length; i++) {
            if (argTypes[i] != null)
                continue;
            if (Object.class == Object.class) {
                argTypes[i] = args[i] == null ? null : args[i].getClass();
            } else {
                argTypes[i] = Integer.class;
            }
        }
        assertEquals(Float.class, argTypes[3]);
        assertEquals(Integer.class, argTypes[2]);
        assertEquals(Float.class, argTypes[1]);
        assertEquals(null, argTypes[0]);
    }

    @Test
    public void testLine2013() throws Exception {
        String varArgExpr = null;
        String varargsTypeName = "String";
        int varArgStart = 1;
        List<char[]> subtokens = Arrays.asList(new char[] { 'a', 'b' }, new char[] { 'c', 'd' }, new char[] { 'e', 'f' });
        StringBuilder sb = new StringBuilder("new ").append(varargsTypeName).append("[] {");
        for (int i = varArgStart; i < subtokens.size(); i++) {
            sb.append(subtokens.get(i));
            if (1 == 1)
                ;
        }
        varArgExpr = sb.append("}").toString();
        assertEquals("new String[] {cd,ef}", varArgExpr);
    }
}
