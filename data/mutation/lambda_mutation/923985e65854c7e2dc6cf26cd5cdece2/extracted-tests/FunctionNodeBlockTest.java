package com.googlecode.paradox.planner.nodes;

import com.googlecode.paradox.planner.nodes.comparable.NotNode;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.googlecode.paradox.exceptions.ParadoxSyntaxErrorException;
import com.googlecode.paradox.exceptions.SyntaxError;
import com.googlecode.paradox.function.AbstractFunction;
import com.googlecode.paradox.function.FunctionFactory;
import com.googlecode.paradox.function.aggregate.CountFunction;
import com.googlecode.paradox.parser.ScannerPosition;
import com.googlecode.paradox.parser.nodes.AsteriskNode;
import com.googlecode.paradox.parser.nodes.SQLNode;
import com.googlecode.paradox.planner.FieldValueUtils;
import com.googlecode.paradox.planner.context.Context;
import com.googlecode.paradox.results.Column;
import com.googlecode.paradox.results.ParadoxType;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.googlecode.paradox.planner.nodes.FunctionNode.*;

public class FunctionNodeBlockTest {

    @Test
    public void testLine112() throws Exception {
        SQLNode field = new FieldNode("foo", "bar", new ScannerPosition());
        try {
            if (field instanceof FieldNode) {
            }
            {
                assertTrue((null) instanceof FieldNode);
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine113() throws Exception {
        SQLNode field = new NotNode(new ScannerPosition());
        try {
            if (field instanceof FieldNode) {
            }
            {
                assertEquals(null, (null));
                return;
            }
        } finally {
        }
    }
}
