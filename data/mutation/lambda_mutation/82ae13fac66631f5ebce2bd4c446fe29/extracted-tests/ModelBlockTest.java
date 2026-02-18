package org.casbin.jcasbin.model;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.casbin.jcasbin.config.Config;
import org.casbin.jcasbin.log.*;
import org.casbin.jcasbin.util.Util;
import java.util.*;
import java.util.regex.Pattern;
import static org.casbin.jcasbin.util.Util.splitCommaDelimited;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.casbin.jcasbin.model.Model.*;

public class ModelBlockTest {

    @Test
    public void testLine333() throws Exception {
        Map<String, Integer> subjectHierarchyMap = new HashMap<String, Integer>() {

            {
                put("foo", 5);
                put("bar", 10);
            }
        };
        ArrayList<String> o1 = new ArrayList<String>(Arrays.asList("1", "2"));
        ArrayList<String> o2 = new ArrayList<String>(Arrays.asList("1", "2"));
        int domainIndex = -1;
        String defaultDomain = "foo";
        try {
            String domain1 = domainIndex != -1 ? o1.get(domainIndex) : defaultDomain;
            String domain2 = domainIndex != -1 ? o2.get(domainIndex) : defaultDomain;
            int priority1 = subjectHierarchyMap.get("foo");
            int priority2 = subjectHierarchyMap.get("bar");
            {
                assertEquals(5, (priority2 - priority1));
                return;
            }
        } finally {
        }
    }
}
