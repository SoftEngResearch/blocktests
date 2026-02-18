package com.microdb.operator;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import com.microdb.exception.DbException;
import com.microdb.model.row.Row;
import com.microdb.model.table.TableDesc;
import com.microdb.model.field.Field;
import com.microdb.model.field.FieldType;
import com.microdb.model.field.IntField;

import java.util.*;

/**
 * 聚合
 *
 * @author zhangjw
 * @version 1.0
 */
public class Aggregate extends Operator {

    /**
     * 待聚合行数据的迭代器
     */
    private final IOperatorIterator tableIterator;

    /**
     * 聚合的字段
     */
    private final int aggregateFieldIndex;

    /**
     * 分组的字段 group by
     */
    private final int groupFieldIndex;

    /**
     * 聚合方式
     */
    private final AggregateType aggregateType;

    /**
     * 聚合后的结果行数据迭代器
     */
    private Iterator<Row> aggResultRowIterator;


    public Aggregate(IOperatorIterator tableIterator, int aggregateFieldIndex, int groupFieldIndex,
                     AggregateType aggregateType) {
        this.tableIterator = tableIterator;
        this.aggregateFieldIndex = aggregateFieldIndex;
        this.groupFieldIndex = groupFieldIndex;
        this.aggregateType = aggregateType;
    }

    @Override
    public void open() throws DbException {
        tableIterator.open();
        List<FieldType> tableFieldTypes = tableIterator.getTableDesc().getFieldTypes();

        FieldType groupByFieldType = tableFieldTypes.get(groupFieldIndex);
        FieldType aggregateFieldType = tableFieldTypes.get(aggregateFieldIndex);

        if (FieldType.INT == aggregateFieldType) {
            if (FieldType.INT == groupByFieldType) {

                // 将数据分组，kv放入容器rowsGrouped中
                TreeMap<Integer, ArrayList<Integer>> rowsGrouped = new TreeMap<>();
                while (tableIterator.hasNext()) {
                    Row row = tableIterator.next();
                    Integer groupFieldValue = ((IntField) row.getField(groupFieldIndex)).getValue();
                    Integer aggregateFieldValue = ((IntField) row.getField(aggregateFieldIndex)).getValue();

                    // TODO 分组逻辑优化
                    rowsGrouped.compute(groupFieldValue, (k, v) -> {
                        // BLOCKTEST EVAL: https://github.com/nothingax/micro-DB/blob/1bbae368f7670f94130606fa65bcbc8ccbb89b98/src/main/java/com/microdb/operator/Aggregate.java#L73-L82
                        // @blocktest("testB").given(v, new ArrayList<>(Arrays.asList(1,2))).given(aggregateFieldValue, 5).checkEq(methodReturn.get(0), 1, 0.001).checkEq(methodReturn.get(1), 2, 0.001).checkEq(methodReturn.get(2), 5, 0.001).checkEq(methodReturn.size(), 3);
                        // @blocktest("testA").given(v, null).given(aggregateFieldValue, 5).checkEq(methodReturn.iterator().next(), 5, 0.001);
                        if (v == null) {
                            ArrayList<Integer> objects = new ArrayList<>();
                            objects.add(aggregateFieldValue);
                            return objects;
                        } else {
                            v.add(aggregateFieldValue);
                            return v;
                        }
                    });
                }

                // 对分组数据做聚合计算，构造新的Row 的迭代器
                ArrayList<Row> aggResultRows = new ArrayList<>();
                for (Map.Entry<Integer, ArrayList<Integer>> entry : rowsGrouped.entrySet()) {
                    Integer groupFieldValue = entry.getKey();
                    ArrayList<Integer> aggregateFieldValues = entry.getValue();
                    // 结果表：由聚合函数字段+分组字段组成
                    TableDesc resultTableDesc = new TableDesc(aggregateFieldType, groupByFieldType);
                    Row row = new Row(resultTableDesc);
                    IntField groupVal = new IntField(groupFieldValue);
                    Field aggregateVal = new IntField(this.calc(aggregateFieldValues));
                    row.setField(0, aggregateVal);
                    row.setField(1, groupVal);
                    aggResultRows.add(row);
                }
                aggResultRowIterator = aggResultRows.iterator();
            } else {
               // TODO 其他类型的group By 字段
            }
        } else {
            // TODO 其他类型的聚合字段
        }

        super.open();
    }

    @Override
    public void close() {
        super.close();
        aggResultRowIterator = null;

    }

    @Override
    public TableDesc getTableDesc() {
        return tableIterator.getTableDesc();
    }

    @Override
    protected Row fetchNextMatched() {
        if (aggResultRowIterator != null && aggResultRowIterator.hasNext()) {
            return aggResultRowIterator.next();
        }
        return null;
    }

    /**
     * 对分组后的数据聚合
     */
    private int calc(ArrayList<Integer> aggregateFieldValues) {
        if (aggregateFieldValues.isEmpty()) {
            throw new IllegalArgumentException("groupBy error: aggregateFieldValues can not be empty");
        }
        switch (aggregateType) {
            case MIN:
                return aggregateFieldValues.stream().mapToInt(x -> x).min().getAsInt();
            // break;
            case MAX:
                return aggregateFieldValues.stream().mapToInt(x -> x).max().getAsInt();
            // break;
            case SUM:
                return aggregateFieldValues.stream().mapToInt(x -> x).sum();
            // break;
            case AVG:
                return (int) aggregateFieldValues.stream().mapToInt(x -> x).average().getAsDouble();
            // break;
            case COUNT:
                return aggregateFieldValues.size();
            // break;
        }
        return 0;
    }
}
