package com.raysonfang.sqltranslator.sql.dialect.oracle.util;

import com.alibaba.druid.sql.ast.SQLRecordDataType;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalDay;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleArgumentExpr;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.alibaba.druid.sql.SQLTransformUtils;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.alibaba.druid.sql.ast.statement.SQLCharacterDataType;
import com.alibaba.druid.util.FnvHash;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.raysonfang.sqltranslator.sql.dialect.oracle.util.OracleSQLDataTypeTransformUtil.*;

public class OracleSQLDataTypeTransformUtilBlockTest {

    @Test
    public void testLine235() throws Exception {
        List<SQLExpr> argumentns = new ArrayList<>(Arrays.asList(new SQLNumberExpr(Double.parseDouble("12"))));
        SQLDataType x = new SQLRecordDataType();
        SQLDataType dataType;
        int len;
        SQLExpr arg0 = argumentns.get(0);
        if (arg0 instanceof SQLNumericLiteralExpr) {
            len = ((SQLNumericLiteralExpr) arg0).getNumber().intValue();
        } else {
            throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
        }
        dataType = new SQLCharacterDataType("nvarchar", len);
        assertEquals(new SQLCharacterDataType("nvarchar", 12), dataType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLine237() throws Exception {
        List<SQLExpr> argumentns = new ArrayList<>(Arrays.asList(new SQLCharExpr("foo")));
        SQLDataType x = new SQLRecordDataType();
        SQLDataType dataType;
        int len;
        SQLExpr arg0 = argumentns.get(0);
        if (arg0 instanceof SQLNumericLiteralExpr) {
            len = ((SQLNumericLiteralExpr) arg0).getNumber().intValue();
        } else {
            throw new UnsupportedOperationException(SQLUtils.toOracleString(x));
        }
        dataType = new SQLCharacterDataType("nvarchar", len);
    }

    @Test
    public void testLine112() throws Exception {
        int precision = 0;
        SQLDataType dataType;
        if (precision < 3) {
            dataType = new SQLDataTypeImpl("tinyint");
        } else if (precision < 5) {
            dataType = new SQLDataTypeImpl("smallint");
        } else if (precision < 9) {
            dataType = new SQLDataTypeImpl("int");
        } else if (precision <= 20) {
            dataType = new SQLDataTypeImpl("bigint");
        } else {
            dataType = new SQLDataTypeImpl("", precision);
        }
        assertEquals(new SQLDataTypeImpl("tinyint"), dataType);
    }

    @Test
    public void testLine114() throws Exception {
        int precision = 3;
        SQLDataType dataType;
        if (precision < 3) {
            dataType = new SQLDataTypeImpl("tinyint");
        } else if (precision < 5) {
            dataType = new SQLDataTypeImpl("smallint");
        } else if (precision < 9) {
            dataType = new SQLDataTypeImpl("int");
        } else if (precision <= 20) {
            dataType = new SQLDataTypeImpl("bigint");
        } else {
            dataType = new SQLDataTypeImpl("", precision);
        }
        assertEquals(new SQLDataTypeImpl("smallint"), dataType);
    }

    @Test
    public void testLine116() throws Exception {
        int precision = 5;
        SQLDataType dataType;
        if (precision < 3) {
            dataType = new SQLDataTypeImpl("tinyint");
        } else if (precision < 5) {
            dataType = new SQLDataTypeImpl("smallint");
        } else if (precision < 9) {
            dataType = new SQLDataTypeImpl("int");
        } else if (precision <= 20) {
            dataType = new SQLDataTypeImpl("bigint");
        } else {
            dataType = new SQLDataTypeImpl("", precision);
        }
        assertEquals(new SQLDataTypeImpl("int"), dataType);
    }

    @Test
    public void testLine118() throws Exception {
        int precision = 9;
        SQLDataType dataType;
        if (precision < 3) {
            dataType = new SQLDataTypeImpl("tinyint");
        } else if (precision < 5) {
            dataType = new SQLDataTypeImpl("smallint");
        } else if (precision < 9) {
            dataType = new SQLDataTypeImpl("int");
        } else if (precision <= 20) {
            dataType = new SQLDataTypeImpl("bigint");
        } else {
            dataType = new SQLDataTypeImpl("", precision);
        }
        assertEquals(new SQLDataTypeImpl("bigint"), dataType);
    }

    @Test
    public void testLine120() throws Exception {
        int precision = 20;
        SQLDataType dataType;
        if (precision < 3) {
            dataType = new SQLDataTypeImpl("tinyint");
        } else if (precision < 5) {
            dataType = new SQLDataTypeImpl("smallint");
        } else if (precision < 9) {
            dataType = new SQLDataTypeImpl("int");
        } else if (precision <= 20) {
            dataType = new SQLDataTypeImpl("bigint");
        } else {
            dataType = new SQLDataTypeImpl("", precision);
        }
        assertEquals(new SQLDataTypeImpl("bigint"), dataType);
    }

    @Test
    public void testLine122() throws Exception {
        int precision = 21;
        SQLDataType dataType;
        if (precision < 3) {
            dataType = new SQLDataTypeImpl("tinyint");
        } else if (precision < 5) {
            dataType = new SQLDataTypeImpl("smallint");
        } else if (precision < 9) {
            dataType = new SQLDataTypeImpl("int");
        } else if (precision <= 20) {
            dataType = new SQLDataTypeImpl("bigint");
        } else {
            dataType = new SQLDataTypeImpl("", precision);
        }
        assertEquals(new SQLDataTypeImpl("decimal", 21), dataType);
    }
}
