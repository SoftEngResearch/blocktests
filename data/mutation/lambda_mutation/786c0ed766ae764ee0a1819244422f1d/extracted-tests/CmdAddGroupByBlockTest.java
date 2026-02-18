package org.v8LogScanner.cmdScanner;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.v8LogScanner.cmdAppl.CmdCommand;
import org.v8LogScanner.rgx.RegExp;
import org.v8LogScanner.rgx.RegExp.PropTypes;
import org.v8LogScanner.rgx.ScanProfile;
import org.v8LogScanner.rgx.ScanProfile.GroupTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.v8LogScanner.cmdScanner.CmdAddGroupBy.*;

public class CmdAddGroupByBlockTest {

    @Test
    public void testLine1() throws Exception {
        RegExp event = new RegExp();
        event.getGroupingProps().add(PropTypes.Event);
        event.getGroupingProps().add(PropTypes.Context);
        event.getGroupingProps().add(PropTypes.Time);
        try {
            List<String> descr = new ArrayList<>();
            {
                assertEquals("[ANY:Context, ANY:Event, ANY:Time]", (descr.stream()).sorted().collect(Collectors.toList()).toString());
                return;
            }
        } finally {
        }
    }
}
