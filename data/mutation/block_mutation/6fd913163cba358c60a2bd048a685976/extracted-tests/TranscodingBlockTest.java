package org.jcodings.transcode;

import org.jcodings.Ptr;
import static java.lang.Byte.toUnsignedInt;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.jcodings.transcode.Transcoding.*;

public class TranscodingBlockTest {

    @Test
    public void testLine252() throws Exception {
        int out_p = 0;
        int ip = 0;
        int recognizedLength = 1;
        int in_p = 3;
        int inchar_start = 2;
        int unitlen = 3;
        EConvResult suspendResult = null;
        int readagain_len = 0;
        int opt;
        byte[] in_bytes;
        Ptr out_pos;
        Ptr in_pos;
        try {
            if (recognizedLength + (in_p - inchar_start) <= unitlen) {
                if (recognizedLength + (in_p - inchar_start) < unitlen) {
                    if (Transcoding.SUSPEND == 0) {
                        assertEquals(null, (suspendResult));
                        return;
                    }
                }
                ip = Transcoding.READ_MORE;
                return;
            } else {
                int invalid_len;
                int discard_len;
                invalid_len = recognizedLength + (in_p - inchar_start);
                discard_len = ((invalid_len - 1) / unitlen) * unitlen;
                readagain_len = invalid_len - discard_len;
                ip = Transcoding.REPORT_INVALID;
                System.out.println(readagain_len);
            }
        } finally {
            assertEquals(0, ip);
        }
    }

    @Test
    public void testLine253() throws Exception {
        int out_p = 0;
        int ip = 0;
        int recognizedLength = 2;
        int in_p = 3;
        int inchar_start = 2;
        int unitlen = 3;
        EConvResult suspendResult = null;
        int readagain_len = 0;
        int opt;
        byte[] in_bytes;
        Ptr out_pos;
        Ptr in_pos;
        try {
            if (recognizedLength + (in_p - inchar_start) <= unitlen) {
                if (recognizedLength + (in_p - inchar_start) < unitlen) {
                    if (Transcoding.SUSPEND == 0)
                        return;
                }
                ip = Transcoding.READ_MORE;
                return;
            } else {
                int invalid_len;
                int discard_len;
                invalid_len = recognizedLength + (in_p - inchar_start);
                discard_len = ((invalid_len - 1) / unitlen) * unitlen;
                readagain_len = invalid_len - discard_len;
                ip = Transcoding.REPORT_INVALID;
                System.out.println(readagain_len);
            }
        } finally {
            assertEquals(Transcoding.READ_MORE, ip);
        }
    }

    @Test
    public void testLine254() throws Exception {
        int out_p = 0;
        int ip = 0;
        int recognizedLength = 12;
        int in_p = 3;
        int inchar_start = 2;
        int unitlen = 3;
        EConvResult suspendResult = null;
        int readagain_len = 0;
        int opt;
        byte[] in_bytes;
        Ptr out_pos;
        Ptr in_pos;
        try {
            if (recognizedLength + (in_p - inchar_start) <= unitlen) {
                if (recognizedLength + (in_p - inchar_start) < unitlen) {
                    if (Transcoding.SUSPEND == 0)
                        return;
                }
                ip = Transcoding.READ_MORE;
                return;
            } else {
                int invalid_len;
                int discard_len;
                invalid_len = recognizedLength + (in_p - inchar_start);
                discard_len = ((invalid_len - 1) / unitlen) * unitlen;
                readagain_len = invalid_len - discard_len;
                ip = Transcoding.REPORT_INVALID;
                System.out.println(readagain_len);
            }
        } finally {
            assertEquals(Transcoding.REPORT_INVALID, ip);
        }
    }

    @Test
    public void testLine255() throws Exception {
        int out_p = 0;
        int ip = 0;
        int recognizedLength = 1;
        int in_p = 3;
        int inchar_start = 2;
        int unitlen = 3;
        EConvResult suspendResult = null;
        int readagain_len = 0;
        int opt;
        byte[] in_bytes;
        Ptr out_pos;
        Ptr in_pos;
        try {
            if (recognizedLength + (in_p - inchar_start) <= unitlen) {
                if (recognizedLength + (in_p - inchar_start) < unitlen) {
                    if (Transcoding.SUSPEND == 1)
                        return;
                }
                ip = Transcoding.READ_MORE;
                return;
            } else {
                int invalid_len;
                int discard_len;
                invalid_len = recognizedLength + (in_p - inchar_start);
                discard_len = ((invalid_len - 1) / unitlen) * unitlen;
                readagain_len = invalid_len - discard_len;
                ip = Transcoding.REPORT_INVALID;
                System.out.println(readagain_len);
            }
        } finally {
            assertEquals(Transcoding.READ_MORE, ip);
        }
    }
}
