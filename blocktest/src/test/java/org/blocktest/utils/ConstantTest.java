package org.blocktest.utils;

import static org.junit.Assert.*;

import com.github.javaparser.ast.expr.AssignExpr;
import java.util.*;
import org.junit.Test;

public class ConstantTest {
  @Test
  public void compoundOperatorSetContainsEveryAssignmentExceptPlainEquals() {
    Set<AssignExpr.Operator> expected = EnumSet.allOf(AssignExpr.Operator.class);
    expected.remove(AssignExpr.Operator.ASSIGN);
    assertEquals(expected, new HashSet<>(Constant.COMPOUND_ASSIGN_OPERATORS));
    assertEquals(expected.size(), Constant.COMPOUND_ASSIGN_OPERATORS.size());
  }

  @Test
  public void primitiveTypeNamesIncludeScalarAndArrayFormsAndDistinctReturnMarkers() {
    for (String name :
        Arrays.asList(
            "int",
            "long",
            "double",
            "float",
            "boolean",
            "char",
            "byte",
            "short",
            "String",
            "java.lang.String")) {
      assertTrue(Constant.PRIMITIVE_TYPES.contains(name));
      assertTrue(Constant.PRIMITIVE_TYPES.contains(name + "[]"));
    }
    assertEquals(20, Constant.PRIMITIVE_TYPES.size());
    assertNotNull(Constant.lambdaReturn);
    assertNotNull(Constant.methodReturn);
    assertNotSame(Constant.lambdaReturn, Constant.methodReturn);
    assertEquals(
        Arrays.asList("GIVEN", "TARGET", "ASSERTION"),
        Arrays.asList(
            Constant.StatementType.GIVEN.name(),
            Constant.StatementType.TARGET.name(),
            Constant.StatementType.ASSERTION.name()));
  }
}
