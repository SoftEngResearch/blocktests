package com.googlecode.paradox.planner.plan;

import com.googlecode.paradox.Driver;
import com.googlecode.paradox.ParadoxConnection;
import com.googlecode.paradox.parser.ScannerPosition;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.googlecode.paradox.ConnectionInfo;
import com.googlecode.paradox.exceptions.*;
import com.googlecode.paradox.metadata.Field;
import com.googlecode.paradox.metadata.Table;
import com.googlecode.paradox.parser.nodes.*;
import com.googlecode.paradox.planner.FieldValueUtils;
import com.googlecode.paradox.planner.context.SelectContext;
import com.googlecode.paradox.planner.nodes.*;
import com.googlecode.paradox.planner.nodes.join.ANDNode;
import com.googlecode.paradox.planner.nodes.join.ORNode;
import com.googlecode.paradox.results.Column;
import com.googlecode.paradox.results.ParadoxType;
import com.googlecode.paradox.utils.FunctionalUtils;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.googlecode.paradox.utils.FunctionalUtils.functionWrapper;
import static com.googlecode.paradox.utils.FunctionalUtils.predicateWrapper;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.googlecode.paradox.planner.plan.SelectPlan.*;

public class SelectPlanBlockTest {

    @Test
    public void testLine1() throws Exception {
        FieldNode fn = new FieldNode("areacodes", "State", new ScannerPosition());
        List<PlanTableNode> tables = new ArrayList<>();
        List<Field> conditionalFields = new ArrayList<>();
        new Driver();
        ParadoxConnection conn = (ParadoxConnection) DriverManager.getConnection("jdbc:paradox:target/test-classes/db");
        tables.add(new PlanTableNode(conn.getConnectionInfo(), new TableNode(null, "areacodes", "alias", null)));
        for (final PlanTableNode table : tables) {
            if (table.isThis(fn.getTableName())) {
                conditionalFields.addAll(Arrays.stream(table.getTable().getFields()).filter(f -> f.getName().equalsIgnoreCase(fn.getName())).collect(Collectors.toSet()));
            }
            continue;
        }
        assertEquals("AREACODES.State", conditionalFields.iterator().next().toString());
    }
}
