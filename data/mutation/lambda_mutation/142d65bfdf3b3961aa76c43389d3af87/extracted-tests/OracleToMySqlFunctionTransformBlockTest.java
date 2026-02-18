package com.raysonfang.sqltranslator.sql.dialect.oracle.function;

import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSysdateExpr;
import com.alibaba.druid.util.StringUtils;
import com.raysonfang.sqltranslator.sql.dialect.mysql.util.MySqlUtil;
import com.raysonfang.sqltranslator.sql.dialect.oracle.util.OracleSQLDataTypeTransformUtil;
import org.springframework.util.ObjectUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.raysonfang.sqltranslator.sql.dialect.oracle.function.OracleToMySqlFunctionTransform.*;

public class OracleToMySqlFunctionTransformBlockTest {

    @Test
    public void testLine117() throws Exception {
        Object e = new SQLPropertyExpr("owner", "name");
        StringBuffer field = new StringBuffer();
        StringBuffer fieldVal = new StringBuffer();
        if (e instanceof SQLPropertyExpr) {
            field.append(((SQLPropertyExpr) e).getName());
        } else if (e instanceof SQLCharExpr) {
            fieldVal.append(" separator '" + ((SQLCharExpr) e).getText());
            fieldVal.append("'");
        }
        assertEquals("name", field.toString());
    }

    @Test
    public void testLine118() throws Exception {
        Object e = new SQLCharExpr("text", null);
        StringBuffer field = new StringBuffer();
        StringBuffer fieldVal = new StringBuffer();
        if (e instanceof SQLPropertyExpr) {
            field.append(((SQLPropertyExpr) e).getName());
        } else if (e instanceof SQLCharExpr) {
            fieldVal.append(" separator '" + ((SQLCharExpr) e).getText());
            fieldVal.append("'");
        }
        assertEquals(" separator 'text'", fieldVal.toString());
    }

    @Test
    public void testLine119() throws Exception {
        Object e = new SQLOrderBy(new SQLIntegerExpr(1));
        StringBuffer field = new StringBuffer();
        StringBuffer fieldVal = new StringBuffer();
        StringBuffer orderBy = new StringBuffer();
        if (e instanceof SQLPropertyExpr) {
            field.append(((SQLPropertyExpr) e).getName());
        } else if (e instanceof SQLCharExpr) {
            fieldVal.append(" separator '" + ((SQLCharExpr) e).getText());
            fieldVal.append("'");
        }
        if (e instanceof SQLOrderBy) {
            ((SQLOrderBy) e).getItems().forEach(c -> {
                // BLOCKTEST EVAL: https://github.com/Isaac315/sql-translator/blob/78f3fb0091c94563a640ef672de3e75e2b5c3bb2/src/main/java/com/raysonfang/sqltranslator/sql/dialect/oracle/function/OracleToMySqlFunctionTransform.java#L116-L118
                blocktest().given(c, new SQLSelectOrderByItem(new SQLDoubleExpr(12))).checkEq(orderBy.toString(), "DOUBLE '12.0'");
                orderBy.append(c.getExpr().toString());
            });
        }
        assertEquals("1", orderBy.toString());
    }

    @Test
    public void testLine128() throws Exception {
        SQLSelectOrderByItem c = new SQLSelectOrderByItem(new SQLDoubleExpr(12));
        StringBuffer orderBy = new StringBuffer();
        orderBy.append(c.getExpr().toString());
        assertEquals("DOUBLE '12.0'", orderBy.toString());
    }
}
