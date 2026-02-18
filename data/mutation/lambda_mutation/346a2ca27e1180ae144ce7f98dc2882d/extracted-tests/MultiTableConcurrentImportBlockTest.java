package io.github.sranka.jdbcimage.main;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.*;
import static io.github.sranka.jdbcimage.main.MultiTableConcurrentImport.*;

public class MultiTableConcurrentImportBlockTest {

    @Test
    public void testLine45() throws Exception {
        java.lang.String x = "importStarted";
        EnumMap<Step, Boolean> enabledSteps = new EnumMap<>(Step.class);
        if (x.startsWith("not-")) {
            Stream.of(Step.class.getEnumConstants()).forEach(s -> enabledSteps.put(s, true));
            enabledSteps.put(Step.valueOf(x.substring("not-".length())), Boolean.FALSE);
        } else {
        }
        assertTrue(enabledSteps.get(Step.importData) == null);
        assertTrue(enabledSteps.get(Step.disableConstraints) == null);
        assertTrue(enabledSteps.get(Step.importStarted));
    }

    @Test
    public void testLine46() throws Exception {
        java.lang.String x = "not-disableConstraints";
        EnumMap<Step, Boolean> enabledSteps = new EnumMap<>(Step.class);
        if (x.startsWith("not-")) {
            Stream.of(Step.class.getEnumConstants()).forEach(s -> enabledSteps.put(s, true));
            enabledSteps.put(Step.valueOf(x.substring("not-".length())), Boolean.FALSE);
        } else {
        }
        assertFalse(enabledSteps.get(Step.disableConstraints));
        assertTrue(enabledSteps.get(Step.importData));
        assertTrue(enabledSteps.get(Step.importStarted));
    }
}
