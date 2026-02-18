package com.adobe.cq.commerce.magento.graphql;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import com.shopify.graphql.support.Input;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.adobe.cq.commerce.magento.graphql.ProductSortInput.*;

public class ProductSortInputBlockTest {

    @Test
    public void testLine1283() throws Exception {
        Serializable filter = new CreateCompareListInput();
        StringBuilder _queryBuilder = new StringBuilder("");
        try {
            Method appendTo = filter.getClass().getMethod("appendTo", StringBuilder.class);
            appendTo.invoke(filter, _queryBuilder);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            /*_queryBuilder.append("null");*/
        }
        assertEquals("{}", _queryBuilder.toString());
    }

    @Test
    public void testLine1286() throws Exception {
        Serializable filter = "foo";
        StringBuilder _queryBuilder = new StringBuilder("");
        try {
            Method appendTo = filter.getClass().getMethod("appendTo", StringBuilder.class);
            appendTo.invoke(filter, _queryBuilder);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            /*_queryBuilder.append("null");*/
        }
        assertEquals("null", _queryBuilder.toString());
    }
}
