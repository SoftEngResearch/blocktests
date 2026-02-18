package org.sonar.plugins.clojure.sensors.eastwood;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.sonar.plugins.clojure.sensors.CommandStreamConsumer;
import org.sonar.plugins.clojure.sensors.Issue;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.sonar.plugins.clojure.sensors.eastwood.EastwoodIssueParser.*;

public class EastwoodIssueParserBlockTest {

    @Test
    public void testLine29() throws Exception {
        java.lang.String line = "X";
        try {
            Matcher matcher = EastwoodIssueParser.EASTWOOD_PATTERN.matcher(line);
            if (matcher.find()) {
                String description = matcher.group(5);
                String filePath = matcher.group(1);
                int lineNumber = Integer.parseInt(matcher.group(2));
                {
                    assertEquals(null, (new Issue(EastwoodIssueParser.EASTWOOD_KEY, filePath, description, lineNumber)));
                    return;
                }
            }
            {
                assertEquals(null, (null));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine30() throws Exception {
        java.lang.String line = "Foo.java:12:5:warning:This is a test message";
        try {
            Matcher matcher = EastwoodIssueParser.EASTWOOD_PATTERN.matcher(line);
            if (matcher.find()) {
                String description = matcher.group(5);
                String filePath = matcher.group(1);
                int lineNumber = Integer.parseInt(matcher.group(2));
                {
                    assertTrue((new Issue(EastwoodIssueParser.EASTWOOD_KEY, filePath, description, lineNumber)) != null);
                    return;
                }
            }
            {
                assertTrue((null) != null);
                return;
            }
        } finally {
        }
    }
}
