package io.github.iamazy.elasticsearch.dsl.sql.parser.query.exact;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import io.github.iamazy.elasticsearch.dsl.sql.enums.SqlConditionOperator;
import io.github.iamazy.elasticsearch.dsl.sql.exception.ElasticSql2DslException;
import io.github.iamazy.elasticsearch.dsl.sql.helper.ElasticSqlArgConverter;
import io.github.iamazy.elasticsearch.dsl.sql.model.AtomicQuery;
import io.github.iamazy.elasticsearch.dsl.sql.model.SqlCondition;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RegexpQueryBuilder;
import org.junit.Test;
import static org.junit.Assert.*;
import static io.github.iamazy.elasticsearch.dsl.sql.parser.query.exact.BinaryQueryParser.*;

public class BinaryQueryParserBlockTest {

    @Test
    public void testLine35() throws Exception {
        java.lang.String queryFieldName = "foo";
        Object[] rightParamValues = new Object[] { "1" };
        SqlConditionOperator operator1 = SqlConditionOperator.Equality;
        try {
            QueryBuilder eqQuery = QueryBuilders.termQuery(queryFieldName, rightParamValues[0]);
            if (SqlConditionOperator.Equality == operator1) {
                {
                    assertEquals("{\n" + "  \"term\" : {\n" + "    \"foo\" : {\n" + "      \"value\" : \"1\",\n" + "      \"boost\" : 1.0\n" + "    }\n" + "  }\n" + "}", (eqQuery).toString());
                    return;
                }
            } else {
                {
                    assertEquals("{\n" + "  \"term\" : {\n" + "    \"foo\" : {\n" + "      \"value\" : \"1\",\n" + "      \"boost\" : 1.0\n" + "    }\n" + "  }\n" + "}", (QueryBuilders.boolQuery().mustNot(eqQuery)).toString());
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine44() throws Exception {
        java.lang.String queryFieldName = "bar";
        Object[] rightParamValues = new Object[] { "12" };
        SqlConditionOperator operator1 = SqlConditionOperator.IsNull;
        try {
            QueryBuilder eqQuery = QueryBuilders.termQuery(queryFieldName, rightParamValues[0]);
            if (SqlConditionOperator.Equality == operator1) {
                {
                    assertEquals("{\n" + "  \"bool\" : {\n" + "    \"must_not\" : [\n" + "      {\n" + "        \"term\" : {\n" + "          \"bar\" : {\n" + "            \"value\" : \"12\",\n" + "            \"boost\" : 1.0\n" + "          }\n" + "        }\n" + "      }\n" + "    ],\n" + "    \"adjust_pure_negative\" : true,\n" + "    \"boost\" : 1.0\n" + "  }\n" + "}", (eqQuery).toString());
                    return;
                }
            } else {
                {
                    assertEquals("{\n" + "  \"bool\" : {\n" + "    \"must_not\" : [\n" + "      {\n" + "        \"term\" : {\n" + "          \"bar\" : {\n" + "            \"value\" : \"12\",\n" + "            \"boost\" : 1.0\n" + "          }\n" + "        }\n" + "      }\n" + "    ],\n" + "    \"adjust_pure_negative\" : true,\n" + "    \"boost\" : 1.0\n" + "  }\n" + "}", (QueryBuilders.boolQuery().mustNot(eqQuery)).toString());
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine118() throws Exception {
        java.lang.String queryFieldName = "foo";
        SqlConditionOperator operator13 = SqlConditionOperator.IsNull;
        try {
            ExistsQueryBuilder existsQuery = QueryBuilders.existsQuery(queryFieldName);
            if (SqlConditionOperator.IsNull == operator13) {
            }
            {
                assertEquals("{\n" + "  \"bool\" : {\n" + "    \"must_not\" : [\n" + "      {\n" + "        \"exists\" : {\n" + "          \"field\" : \"foo\",\n" + "          \"boost\" : 1.0\n" + "        }\n" + "      }\n" + "    ],\n" + "    \"adjust_pure_negative\" : true,\n" + "    \"boost\" : 1.0\n" + "  }\n" + "}", (existsQuery).toString());
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine132() throws Exception {
        java.lang.String queryFieldName = "foo";
        SqlConditionOperator operator13 = SqlConditionOperator.IsNotNull;
        try {
            ExistsQueryBuilder existsQuery = QueryBuilders.existsQuery(queryFieldName);
            if (SqlConditionOperator.IsNull == operator13) {
            }
            {
                assertEquals("{\n" + "  \"exists\" : {\n" + "    \"field\" : \"foo\",\n" + "    \"boost\" : 1.0\n" + "  }\n" + "}", (existsQuery).toString());
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine153() throws Exception {
        SQLCharExpr rightExpr = new SQLCharExpr("abc%def_");
        SQLExpr leftExpr = new SQLCharExpr("name");
        SqlConditionOperator operator1 = SqlConditionOperator.Like;
        try {
            String rightText = rightExpr.getText();
            if (rightExpr.getText().contains("%")) {
                rightText = rightText.replace("%", "*");
            }
            if (rightExpr.getText().contains("_")) {
                rightText = rightText.replace("_", "?");
            }
            RegexpQueryBuilder regexpQueryBuilder = QueryBuilders.regexpQuery(leftExpr.toString(), rightText);
            if (operator1.equals(SqlConditionOperator.Like)) {
                {
                    assertEquals(QueryBuilders.regexpQuery("'name'", "abc*def?"), (regexpQueryBuilder));
                    return;
                }
            } else {
                {
                    assertEquals(QueryBuilders.regexpQuery("'name'", "abc*def?"), (QueryBuilders.boolQuery().mustNot(regexpQueryBuilder)));
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine155() throws Exception {
        SQLCharExpr rightExpr = new SQLCharExpr("abc%def_");
        SQLExpr leftExpr = new SQLCharExpr("name");
        SqlConditionOperator operator1 = SqlConditionOperator.NotEqual;
        try {
            String rightText = rightExpr.getText();
            if (rightExpr.getText().contains("%")) {
                rightText = rightText.replace("%", "*");
            }
            if (rightExpr.getText().contains("_")) {
                rightText = rightText.replace("_", "?");
            }
            RegexpQueryBuilder regexpQueryBuilder = QueryBuilders.regexpQuery(leftExpr.toString(), rightText);
            if (operator1.equals(SqlConditionOperator.Like)) {
                {
                    assertEquals(QueryBuilders.boolQuery().mustNot(QueryBuilders.regexpQuery("'name'", "abc*def?")), (regexpQueryBuilder));
                    return;
                }
            } else {
                {
                    assertEquals(QueryBuilders.boolQuery().mustNot(QueryBuilders.regexpQuery("'name'", "abc*def?")), (QueryBuilders.boolQuery().mustNot(regexpQueryBuilder)));
                    return;
                }
            }
        } finally {
        }
    }
}
