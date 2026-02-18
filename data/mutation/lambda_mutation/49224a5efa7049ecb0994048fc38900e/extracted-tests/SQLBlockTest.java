package com.gysoft.jdbc.bean;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.gysoft.jdbc.dao.EntityDao;
import com.gysoft.jdbc.tools.CollectionUtil;
import com.gysoft.jdbc.tools.EntityTools;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.gysoft.jdbc.bean.SQL.*;

public class SQLBlockTest {

    @Test
    public void testLine114() throws Exception {
        SQLPiepline.SQLNext sqlNext = new SQLPiepline.SQLNext(new SQL("a"), "U");
        List<SQL> subSqls = new ArrayList<>();
        SQL s = sqlNext.getSql();
        if (sqlNext.getUnionType() != null) {
            s.setUnionType(sqlNext.getUnionType());
        } else {
            s.setUnionType(",");
        }
        subSqls.add(s);
        assertEquals("U", subSqls.iterator().next().getUnionType());
        assertTrue(subSqls.size() == 1);
    }

    @Test
    public void testLine117() throws Exception {
        SQLPiepline.SQLNext sqlNext = new SQLPiepline.SQLNext(new SQL("a"), null);
        List<SQL> subSqls = new ArrayList<>();
        SQL s = sqlNext.getSql();
        if (sqlNext.getUnionType() != null) {
            s.setUnionType(sqlNext.getUnionType());
        } else {
            s.setUnionType(",");
        }
        subSqls.add(s);
        assertEquals(",", subSqls.iterator().next().getUnionType());
        assertTrue(subSqls.size() == 1);
    }
}
