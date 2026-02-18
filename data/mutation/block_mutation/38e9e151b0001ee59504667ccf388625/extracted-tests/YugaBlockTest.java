package com.twelfthmile.yuga;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.twelfthmile.yuga.types.*;
import com.twelfthmile.yuga.utils.FsaContextMap;
import com.twelfthmile.yuga.utils.L;
import com.twelfthmile.yuga.utils.Util;
import com.twelfthmile.yuga.utils.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import static com.twelfthmile.yuga.Yuga.*;

public class YugaBlockTest {

    @Test
    public void testLine1() throws Exception {
        int state = -1;
        int i = 1;
        FsaContextMap map = new FsaContextMap();
        map.setIndex(10);
        map.setType(Constants.TY_CALLFORWARD);
        try {
            if (map.getType() == null)
                return;
            if (state == -1 && map.getType().equals(Constants.TY_CALLFORWARD))
                i = map.getIndex();
            else if (state == -1 && !map.getType().equals(Constants.TY_PCT))
                i = i - 1;
        } finally {
            assertEquals(10, i);
        }
    }
}
