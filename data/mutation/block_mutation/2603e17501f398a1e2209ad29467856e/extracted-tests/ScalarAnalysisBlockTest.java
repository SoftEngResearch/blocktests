package com.esotericsoftware.yamlbeans.emitter;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.types.Flow.IfStmt;
import static org.blocktest.utils.Constant.*;
import java.util.regex.Pattern;
import com.esotericsoftware.yamlbeans.constants.Unicode;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.esotericsoftware.yamlbeans.emitter.ScalarAnalysis.*;

public class ScalarAnalysisBlockTest {

    @Test
    public void testLine96() throws Exception {
        char ceh = '!';
        boolean flowIndicators = false;
        boolean blockIndicators = false;
        boolean followedBySpace = false;
        if (0 != -1) {
            flowIndicators = true;
            blockIndicators = true;
        }
        if (ceh == '?' || ceh == ':') {
            flowIndicators = true;
            if (followedBySpace)
                blockIndicators = true;
        }
        if (ceh == '-' && followedBySpace) {
            flowIndicators = true;
            blockIndicators = true;
        }
        assertTrue(blockIndicators);
        assertTrue(flowIndicators);
    }

    @Test
    public void testLine100() throws Exception {
        char ceh = '?';
        boolean followedBySpace = true;
        boolean flowIndicators = false;
        boolean blockIndicators = false;
        if (-1 != -1) {
            flowIndicators = true;
            blockIndicators = true;
        }
        if (ceh == '?' || ceh == ':') {
            flowIndicators = true;
            if (followedBySpace)
                blockIndicators = true;
        }
        if (ceh == '-' && followedBySpace) {
            flowIndicators = true;
            blockIndicators = true;
        }
        assertTrue(blockIndicators);
        assertTrue(flowIndicators);
    }

    @Test
    public void testLine104() throws Exception {
        char ceh = ':';
        boolean followedBySpace = false;
        boolean flowIndicators = false;
        boolean blockIndicators = false;
        if (-1 != -1) {
            flowIndicators = true;
            blockIndicators = true;
        }
        if (ceh == '?' || ceh == ':') {
            flowIndicators = true;
            if (followedBySpace)
                blockIndicators = true;
        }
        if (ceh == '-' && followedBySpace) {
            flowIndicators = true;
            blockIndicators = true;
        }
        assertFalse(blockIndicators);
        assertTrue(flowIndicators);
    }

    @Test
    public void testLine108() throws Exception {
        char ceh = '-';
        boolean followedBySpace = true;
        boolean flowIndicators = false;
        boolean blockIndicators = false;
        if (-1 != -1) {
            flowIndicators = true;
            blockIndicators = true;
        }
        if (ceh == '?' || ceh == ':') {
            flowIndicators = true;
            if (followedBySpace)
                blockIndicators = true;
        }
        if (ceh == '-' && followedBySpace) {
            flowIndicators = true;
            blockIndicators = true;
        }
        assertTrue(blockIndicators);
        assertTrue(flowIndicators);
    }

    @Test
    public void testLine112() throws Exception {
        char ceh = 'A';
        boolean flowIndicators = false;
        boolean blockIndicators = false;
        boolean followedBySpace = false;
        if (-1 != -1) {
            flowIndicators = true;
            blockIndicators = true;
        }
        if (ceh == '?' || ceh == ':') {
            flowIndicators = true;
            if (followedBySpace)
                blockIndicators = true;
        }
        if (ceh == '-' && followedBySpace) {
            flowIndicators = true;
            blockIndicators = true;
        }
        assertFalse(blockIndicators);
        assertFalse(flowIndicators);
    }

    @Test
    public void testLine183() throws Exception {
        boolean spaces = true;
        boolean breaks = true;
        boolean leading = false;
        boolean trailingSpaces = false;
        boolean leadingSpaces = false;
        boolean trailingBreaks = false;
        boolean leadingBreaks = false;
        boolean mixedBreaksSpaces = false;
        boolean[] _blocktest_flow_testLine183 = new boolean[0];
        if (spaces && breaks)
            mixedBreaksSpaces = true;
        else if (spaces) {
            trailingSpaces = true;
            if (leading)
                leadingSpaces = true;
        } else if (breaks) {
            trailingBreaks = true;
            if (leading)
                leadingBreaks = true;
        }
        for (int _blocktest_testLine183_i = 0; _blocktest_testLine183_i < 0; _blocktest_testLine183_i += 1) {
            assertTrue(_blocktest_flow_testLine183[_blocktest_testLine183_i]);
        }
    }
}
