package org.opensky.libadsb;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.opensky.libadsb.exceptions.BadFormatException;
import org.opensky.libadsb.exceptions.UnspecifiedFormatError;
import org.opensky.libadsb.msgs.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.opensky.libadsb.ModeSDecoder.*;

public class ModeSDecoderBlockTest {

    @Test
    public void testLine152() throws Exception {
        ExtendedSquitter es1090 = new ExtendedSquitter(new ModeSReply("8d4d0131f82100020049b8209514"));
        int dd__adsbVersion = 0;
        int subtype = (es1090.getMessage()[0] >>> 1) & 0x3;
        boolean hasMe11Bit = (es1090.getMessage()[1] & 0x20) != 0;
        if (subtype == 1 && (dd__adsbVersion > 0 || !hasMe11Bit)) {
        }
    }
}
