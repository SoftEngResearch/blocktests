package com.deepoove.swagger.diff.compare;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.deepoove.swagger.diff.model.ElProperty;
import io.swagger.models.ArrayModel;
import io.swagger.models.Model;
import io.swagger.models.RefModel;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.deepoove.swagger.diff.compare.ModelDiff.*;

public class ModelDiffBlockTest {

    @Test
    public void testLine136() throws Exception {
        java.lang.String enumVal = "foo";
        ArrayList<java.lang.String> t = new ArrayList<>(Arrays.asList("bar", "fA", "FAA", "faw", "foO", "ffo"));
        try {
            for (String value : t) {
                if (1 == 1) {
                    {
                        assertEquals("foO", (value));
                        return;
                    }
                }
            }
            {
                assertEquals("foO", (null));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine137() throws Exception {
        java.lang.String enumVal = "xoo";
        ArrayList<java.lang.String> t = new ArrayList<>(Arrays.asList("bar", "fA", "FAA", "faw", "foO", "ffo"));
        try {
            for (String value : t) {
                if (1 == 1) {
                    {
                        assertEquals(null, (value));
                        return;
                    }
                }
            }
            {
                assertEquals(null, (null));
                return;
            }
        } finally {
        }
    }
}
