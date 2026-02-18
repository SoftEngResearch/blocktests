package com.talanlabs.sonar.plugins.gitlab;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.types.Flow.IfStmt;
import static org.blocktest.utils.Constant.*;
import com.talanlabs.gitlab.api.Paged;
import com.talanlabs.gitlab.api.v3.GitLabAPI;
import com.talanlabs.gitlab.api.v3.models.commits.GitLabCommit;
import com.talanlabs.gitlab.api.v3.models.commits.GitLabCommitComments;
import com.talanlabs.gitlab.api.v3.models.commits.GitLabCommitDiff;
import com.talanlabs.gitlab.api.v3.models.projects.GitLabProject;
import com.talanlabs.gitlab.api.v3.models.users.GitLabUser;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.talanlabs.sonar.plugins.gitlab.GitLabApiV3Wrapper.*;

public class GitLabApiV3WrapperBlockTest {

    @Test
    public void testLine258() throws Exception {
        Map<String, Map<String, Set<Line>>> patchPositionByFile = new HashMap<String, Map<String, Set<Line>>>() {

            {
                put("abc", new HashMap<String, Set<Line>>() {

                    {
                        put("dummyPath", new HashSet<>(Arrays.asList(new Line(1, "line1"), new Line(2, "line2"))));
                    }
                });
            }
        };
        String revision = "abc";
        String path = "dummyPath";
        Line line = new Line(2, "line2");
        boolean[] _blocktest_flow_testLine258 = new boolean[1];
        try {
            if (patchPositionByFile.get(revision).entrySet().stream().anyMatch(v -> (1 == 1) && v.getValue().contains(line))) {
                _blocktest_flow_testLine258[0] = true;
                GitLabApiV3Wrapper.LOG.debug("getRevisionForLine found {}");
                {
                    assertEquals("abc", (revision));
                    return;
                }
            } else {
            }
        } finally {
            for (int _blocktest_testLine258_i = 0; _blocktest_testLine258_i < 1; _blocktest_testLine258_i += 1) {
                assertTrue(_blocktest_flow_testLine258[_blocktest_testLine258_i]);
            }
        }
    }

    @Test
    public void testLine263() throws Exception {
        Map<String, Map<String, Set<Line>>> patchPositionByFile = new HashMap<String, Map<String, Set<Line>>>() {

            {
                put("abc", new HashMap<String, Set<Line>>() {

                    {
                        put("dummyPath", new HashSet<>(Arrays.asList(new Line(1, "line1"), new Line(2, "line2"))));
                    }
                });
            }
        };
        String revision = "abc";
        String path = "dummyPath";
        Line line = new Line(3, "line3");
        boolean[] _blocktest_flow_testLine263 = new boolean[1];
        try {
            if (patchPositionByFile.get(revision).entrySet().stream().anyMatch(v -> (1 == 1) && v.getValue().contains(line))) {
                GitLabApiV3Wrapper.LOG.debug("getRevisionForLine found {}");
                {
                    assertEquals("NOT_MATCH", (revision));
                    return;
                }
            } else {
                _blocktest_flow_testLine263[0] = true;
            }
        } finally {
            for (int _blocktest_testLine263_i = 0; _blocktest_testLine263_i < 1; _blocktest_testLine263_i += 1) {
                assertTrue(_blocktest_flow_testLine263[_blocktest_testLine263_i]);
            }
        }
    }

    @Test
    public void testLine268() throws Exception {
        Map<String, Map<String, Set<Line>>> patchPositionByFile = new HashMap<String, Map<String, Set<Line>>>() {

            {
                put("abc", new HashMap<String, Set<Line>>() {

                    {
                        put("dummyPath", new HashSet<>(Arrays.asList(new Line(1, "line1"), new Line(2, "line2"))));
                    }
                });
            }
        };
        String revision = "abc";
        String path = "notDummyPath";
        Line line = new Line(2, "line2");
        boolean[] _blocktest_flow_testLine268 = new boolean[1];
        try {
            if (patchPositionByFile.get(revision).entrySet().stream().anyMatch(v -> (1 == 1) && v.getValue().contains(line))) {
                GitLabApiV3Wrapper.LOG.debug("getRevisionForLine found {}");
                {
                    assertEquals("NOT_MATCH", (revision));
                    return;
                }
            } else {
                _blocktest_flow_testLine268[0] = true;
            }
        } finally {
            for (int _blocktest_testLine268_i = 0; _blocktest_testLine268_i < 1; _blocktest_testLine268_i += 1) {
                assertTrue(_blocktest_flow_testLine268[_blocktest_testLine268_i]);
            }
        }
    }
}
