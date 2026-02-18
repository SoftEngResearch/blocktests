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

public class EastwoodIssueParser {
    private static final Pattern EASTWOOD_PATTERN = Pattern.compile("([^:]+):(\\d+):(\\d+):([\\s\\w-]+):(.*)");
    public static final String EASTWOOD_KEY = "eastwood";

    private EastwoodIssueParser() {}

    public static List<Issue> parse(CommandStreamConsumer output) {
        if (output != null) {
            return output.getData().stream().map(line -> {
                // BLOCKTEST EVAL: https://github.com/fsantiag/sonar-clojure/blob/8935e2291d21d57922006bab2f7fdd82d08e415e/src/main/java/org/sonar/plugins/clojure/sensors/eastwood/EastwoodIssueParser.java#L22-L31
                blocktest().given(line, "X").checkReturnEq(null);
                blocktest().given(line, "Foo.java:12:5:warning:This is a test message").checkTrue(lambdaReturn != null);
                Matcher matcher = EASTWOOD_PATTERN.matcher(line);
                if (matcher.find()) {
                    String description = matcher.group(5);
                    String filePath = matcher.group(1);
                    int lineNumber = Integer.parseInt(matcher.group(2));
                    return new Issue(EASTWOOD_KEY, description, filePath, lineNumber);
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
