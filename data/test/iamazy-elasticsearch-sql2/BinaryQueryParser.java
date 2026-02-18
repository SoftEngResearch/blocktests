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


public class BinaryQueryParser extends AbstractExactQueryParser {

    public AtomicQuery parseBinaryQuery(SQLBinaryOpExpr binQueryExpr, String queryAs) {
        SQLBinaryOperator binaryOperator = binQueryExpr.getOperator();

        //EQ NEQ
        if (SQLBinaryOperator.Equality == binaryOperator || SQLBinaryOperator.LessThanOrGreater == binaryOperator || SQLBinaryOperator.NotEqual == binaryOperator) {
            Object targetVal = ElasticSqlArgConverter.convertSqlArg(binQueryExpr.getRight());
            SqlConditionOperator operator = SQLBinaryOperator.Equality == binaryOperator ? SqlConditionOperator.Equality : SqlConditionOperator.NotEqual;
            return parseCondition(binQueryExpr.getLeft(), operator, new Object[]{targetVal}, queryAs, (queryFieldName, operator1, rightParamValues) -> {
                // BLOCKTEST EVAL: https://github.com/iamazy/elasticsearch-sql2/blob/05a7743fb2bb70816f8255a7e7b3086374b15d8c/src/main/java/io/github/iamazy/elasticsearch/dsl/sql/parser/query/exact/BinaryQueryParser.java#L28-L36
                blocktest().given(queryFieldName, "foo").given(rightParamValues, new Object[]{"1"}).given(operator1, SqlConditionOperator.Equality)
                        .checkEq(methodReturn.toString(), "{\n" +
                                "  \"term\" : {\n" +
                                "    \"foo\" : {\n" +
                                "      \"value\" : \"1\",\n" +
                                "      \"boost\" : 1.0\n" +
                                "    }\n" +
                                "  }\n" +
                                "}");
                blocktest().given(queryFieldName, "bar").given(rightParamValues, new Object[]{"12"}).given(operator1, SqlConditionOperator.IsNull)
                        .checkEq(methodReturn.toString(), "{\n" +
                                "  \"bool\" : {\n" +
                                "    \"must_not\" : [\n" +
                                "      {\n" +
                                "        \"term\" : {\n" +
                                "          \"bar\" : {\n" +
                                "            \"value\" : \"12\",\n" +
                                "            \"boost\" : 1.0\n" +
                                "          }\n" +
                                "        }\n" +
                                "      }\n" +
                                "    ],\n" +
                                "    \"adjust_pure_negative\" : true,\n" +
                                "    \"boost\" : 1.0\n" +
                                "  }\n" +
                                "}");
                QueryBuilder eqQuery = QueryBuilders.termQuery(queryFieldName, rightParamValues[0]);
                if (SqlConditionOperator.Equality == operator1) {
                    return eqQuery;
                }
                else {
                    return QueryBuilders.boolQuery().mustNot(eqQuery);
                }
            });
        }



        //GT GTE LT LTE
        if (SQLBinaryOperator.GreaterThan == binaryOperator || SQLBinaryOperator.GreaterThanOrEqual == binaryOperator
                || SQLBinaryOperator.LessThan == binaryOperator || SQLBinaryOperator.LessThanOrEqual == binaryOperator) {

            SqlConditionOperator operator;
            if (SQLBinaryOperator.GreaterThan == binaryOperator) {
                operator = SqlConditionOperator.GreaterThan;
            }
            else if (SQLBinaryOperator.GreaterThanOrEqual == binaryOperator) {
                operator = SqlConditionOperator.GreaterThanOrEqual;
            }
            else if (SQLBinaryOperator.LessThan == binaryOperator) {
                operator = SqlConditionOperator.LessThan;
            }
            else {
                operator = SqlConditionOperator.LessThanOrEqual;
            }

            Object targetVal = ElasticSqlArgConverter.convertSqlArg(binQueryExpr.getRight());
            return parseCondition(binQueryExpr.getLeft(), operator, new Object[]{targetVal}, queryAs, (queryFieldName, operator12, rightParamValues) -> {
                QueryBuilder rangeQuery = null;
                if (SqlConditionOperator.GreaterThan == operator12) {
                    rangeQuery = QueryBuilders.rangeQuery(queryFieldName).gt(rightParamValues[0]);
                }
                else if (SqlConditionOperator.GreaterThanOrEqual == operator12) {
                    rangeQuery = QueryBuilders.rangeQuery(queryFieldName).gte(rightParamValues[0]);
                }
                else if (SqlConditionOperator.LessThan == operator12) {
                    rangeQuery = QueryBuilders.rangeQuery(queryFieldName).lt(rightParamValues[0]);
                }
                else if (SqlConditionOperator.LessThanOrEqual == operator12) {
                    rangeQuery = QueryBuilders.rangeQuery(queryFieldName).lte(rightParamValues[0]);
                }
                return rangeQuery;
            });
        }

        //IS / IS NOT
        if (SQLBinaryOperator.Is == binaryOperator || SQLBinaryOperator.IsNot == binaryOperator) {
            if (!(binQueryExpr.getRight() instanceof SQLNullExpr)) {
                throw new ElasticSql2DslException("[syntax error] Is/IsNot expr right part should be null");
            }
            SqlConditionOperator operator = SQLBinaryOperator.Is == binaryOperator ? SqlConditionOperator.IsNull : SqlConditionOperator.IsNotNull;
            return parseCondition(binQueryExpr.getLeft(), operator, null, queryAs, (queryFieldName, operator13, rightParamValues) -> {
                // BLOCKTEST EVAL: https://github.com/iamazy/elasticsearch-sql2/blob/05a7743fb2bb70816f8255a7e7b3086374b15d8c/src/main/java/io/github/iamazy/elasticsearch/dsl/sql/parser/query/exact/BinaryQueryParser.java#L84-L90
                blocktest().given(queryFieldName, "foo").given(operator13, SqlConditionOperator.IsNull).checkEq(methodReturn.toString(), "{\n" +
                        "  \"bool\" : {\n" +
                        "    \"must_not\" : [\n" +
                        "      {\n" +
                        "        \"exists\" : {\n" +
                        "          \"field\" : \"foo\",\n" +
                        "          \"boost\" : 1.0\n" +
                        "        }\n" +
                        "      }\n" +
                        "    ],\n" +
                        "    \"adjust_pure_negative\" : true,\n" +
                        "    \"boost\" : 1.0\n" +
                        "  }\n" +
                        "}");
                blocktest().given(queryFieldName, "foo").given(operator13, SqlConditionOperator.IsNotNull).checkEq(methodReturn.toString(), "{\n" +
                        "  \"exists\" : {\n" +
                        "    \"field\" : \"foo\",\n" +
                        "    \"boost\" : 1.0\n" +
                        "  }\n" +
                        "}");
                ExistsQueryBuilder existsQuery = QueryBuilders.existsQuery(queryFieldName);
                if (SqlConditionOperator.IsNull == operator13) {
                    return QueryBuilders.boolQuery().mustNot(existsQuery);
                }
                return existsQuery;
            });
        }

        if(SQLBinaryOperator.Like == binaryOperator || SQLBinaryOperator.NotLike == binaryOperator){
            if(binQueryExpr.getRight() instanceof SQLCharExpr) {
                SQLCharExpr rightExpr = (SQLCharExpr) binQueryExpr.getRight();
                SQLExpr leftExpr = binQueryExpr.getLeft();
                SqlConditionOperator operator=SQLBinaryOperator.Like == binaryOperator?SqlConditionOperator.Like:SqlConditionOperator.NotLike;
                return parseCondition(binQueryExpr.getLeft(),operator,null,queryAs,((queryFieldName, operator1, rightParamValues) -> {
                    // BLOCKTEST EVAL: https://github.com/iamazy/elasticsearch-sql2/blob/05a7743fb2bb70816f8255a7e7b3086374b15d8c/src/main/java/io/github/iamazy/elasticsearch/dsl/sql/parser/query/exact/BinaryQueryParser.java#L98-L112
                    blocktest().given(rightExpr, new SQLCharExpr("abc%def_")).given(leftExpr, new SQLCharExpr("name")).given(operator1, SqlConditionOperator.Like)
                            .checkReturnEq(QueryBuilders.regexpQuery("'name'", "abc*def?"));
                    blocktest().given(rightExpr, new SQLCharExpr("abc%def_")).given(leftExpr, new SQLCharExpr("name")).given(operator1, SqlConditionOperator.NotEqual)
                            .checkReturnEq(QueryBuilders.boolQuery().mustNot(QueryBuilders.regexpQuery("'name'", "abc*def?")));

                    String rightText=rightExpr.getText();
                    if(rightExpr.getText().contains("%")){
                        rightText=rightText.replace("%","*");
                    }
                    if(rightExpr.getText().contains("_")){
                        rightText=rightText.replace("_","?");
                    }
                    RegexpQueryBuilder regexpQueryBuilder = QueryBuilders.regexpQuery(leftExpr.toString(), rightText);
                    if(operator1.equals(SqlConditionOperator.Like)) {
                        return regexpQueryBuilder;
                    }else{
                        return QueryBuilders.boolQuery().mustNot(regexpQueryBuilder);
                    }
                }));
            }else{
                throw new ElasticSql2DslException("[syntax error] Like/NotLike expr right part should be a char expr");
            }
        }

        throw new ElasticSql2DslException(String.format("[syntax error] Can not support binary query type[%s]", binQueryExpr.toString()));
    }
}
