package cn.thare.feqb.helper;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import cn.thare.feqb.annotation.func.aggs.CardinalityAggregation;
import cn.thare.feqb.annotation.func.aggs.ExtendedStatsAggregation;
import cn.thare.feqb.annotation.func.aggs.StatsAggregation;
import cn.thare.feqb.annotation.func.aggs.TermsAggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.StatsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStatsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.*;
import static cn.thare.feqb.helper.AggregationHelper.*;

public class AggregationHelperBlockTest {

    @Test
    public void testLine49() throws Exception {
        TermsAggregation.Order t = TermsAggregation.Order.COUNT_ASC;
        try {
            switch(t) {
                case COUNT_ASC:
                    {
                        assertEquals(BucketOrder.count(Boolean.TRUE), (BucketOrder.count(Boolean.TRUE)));
                        return;
                    }
                case COUNT_DESC:
                    {
                        assertEquals(BucketOrder.count(Boolean.TRUE), (BucketOrder.count(Boolean.FALSE)));
                        return;
                    }
                case KEY_ASC:
                    {
                        assertEquals(BucketOrder.count(Boolean.TRUE), (BucketOrder.key(Boolean.TRUE)));
                        return;
                    }
                case KEY_DESC:
                /*return BucketOrder.key(Boolean.FALSE);*/
                default:
                    {
                        assertEquals(BucketOrder.count(Boolean.TRUE), (null));
                        return;
                    }
            }
        } finally {
        }
    }

    @Test
    public void testLine50() throws Exception {
        TermsAggregation.Order t = TermsAggregation.Order.COUNT_DESC;
        try {
            switch(t) {
                case COUNT_ASC:
                    {
                        assertEquals(BucketOrder.count(Boolean.FALSE), (BucketOrder.count(Boolean.TRUE)));
                        return;
                    }
                case COUNT_DESC:
                    {
                        assertEquals(BucketOrder.count(Boolean.FALSE), (BucketOrder.count(Boolean.FALSE)));
                        return;
                    }
                case KEY_ASC:
                    {
                        assertEquals(BucketOrder.count(Boolean.FALSE), (BucketOrder.key(Boolean.TRUE)));
                        return;
                    }
                case KEY_DESC:
                /*return BucketOrder.key(Boolean.FALSE);*/
                default:
                    {
                        assertEquals(BucketOrder.count(Boolean.FALSE), (null));
                        return;
                    }
            }
        } finally {
        }
    }

    @Test
    public void testLine51() throws Exception {
        TermsAggregation.Order t = TermsAggregation.Order.KEY_ASC;
        try {
            switch(t) {
                case COUNT_ASC:
                    {
                        assertEquals(BucketOrder.key(Boolean.TRUE), (BucketOrder.count(Boolean.TRUE)));
                        return;
                    }
                case COUNT_DESC:
                    {
                        assertEquals(BucketOrder.key(Boolean.TRUE), (BucketOrder.count(Boolean.FALSE)));
                        return;
                    }
                case KEY_ASC:
                    {
                        assertEquals(BucketOrder.key(Boolean.TRUE), (BucketOrder.key(Boolean.TRUE)));
                        return;
                    }
                case KEY_DESC:
                /*return BucketOrder.key(Boolean.FALSE);*/
                default:
                    {
                        assertEquals(BucketOrder.key(Boolean.TRUE), (null));
                        return;
                    }
            }
        } finally {
        }
    }

    @Test
    public void testLine52() throws Exception {
        TermsAggregation.Order t = TermsAggregation.Order.KEY_DESC;
        try {
            switch(t) {
                case COUNT_ASC:
                    {
                        assertEquals(BucketOrder.key(Boolean.FALSE), (BucketOrder.count(Boolean.TRUE)));
                        return;
                    }
                case COUNT_DESC:
                    {
                        assertEquals(BucketOrder.key(Boolean.FALSE), (BucketOrder.count(Boolean.FALSE)));
                        return;
                    }
                case KEY_ASC:
                    {
                        assertEquals(BucketOrder.key(Boolean.FALSE), (BucketOrder.key(Boolean.TRUE)));
                        return;
                    }
                case KEY_DESC:
                /*return BucketOrder.key(Boolean.FALSE);*/
                default:
                    {
                        assertEquals(BucketOrder.key(Boolean.FALSE), (null));
                        return;
                    }
            }
        } finally {
        }
    }
}
