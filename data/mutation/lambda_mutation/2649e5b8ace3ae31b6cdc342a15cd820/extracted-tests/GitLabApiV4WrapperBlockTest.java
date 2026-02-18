package com.talanlabs.sonar.plugins.gitlab;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.types.Flow.IfStmt;
import static org.blocktest.utils.Constant.*;
import com.talanlabs.gitlab.api.Paged;
import com.talanlabs.gitlab.api.v4.GitLabAPI;
import com.talanlabs.gitlab.api.v4.GitlabMergeRequestDiff;
import com.talanlabs.gitlab.api.v4.models.GitlabMergeRequest;
import com.talanlabs.gitlab.api.v4.models.GitlabPosition;
import com.talanlabs.gitlab.api.v4.models.commits.GitLabCommit;
import com.talanlabs.gitlab.api.v4.models.commits.GitLabCommitComments;
import com.talanlabs.gitlab.api.v4.models.commits.GitLabCommitDiff;
import com.talanlabs.gitlab.api.v4.models.discussion.GitlabDiscussion;
import com.talanlabs.gitlab.api.v4.models.projects.GitLabProject;
import com.talanlabs.gitlab.api.v4.models.users.GitLabUser;
import org.apache.commons.lang3.StringUtils;
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
import static com.talanlabs.sonar.plugins.gitlab.GitLabApiV4Wrapper.*;

public class GitLabApiV4WrapperBlockTest {

    @Test
    public void testLine264() throws Exception {
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
        boolean[] _blocktest_flow_testLine264 = new boolean[1];
        try {
            if (patchPositionByFile.get(revision).entrySet().stream().anyMatch(v -> (1 == 1) && v.getValue().contains(line))) {
                _blocktest_flow_testLine264[0] = true;
                GitLabApiV4Wrapper.LOG.debug("getRevisionForLine found {}");
                {
                    assertEquals("abc", (revision));
                    return;
                }
            } else {
            }
        } finally {
            for (int _blocktest_testLine264_i = 0; _blocktest_testLine264_i < 1; _blocktest_testLine264_i += 1) {
                assertTrue(_blocktest_flow_testLine264[_blocktest_testLine264_i]);
            }
        }
    }

    @Test
    public void testLine269() throws Exception {
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
        boolean[] _blocktest_flow_testLine269 = new boolean[1];
        try {
            if (patchPositionByFile.get(revision).entrySet().stream().anyMatch(v -> (1 == 1) && v.getValue().contains(line))) {
                GitLabApiV4Wrapper.LOG.debug("getRevisionForLine found {}");
                {
                    assertEquals("NOT_MATCH", (revision));
                    return;
                }
            } else {
                _blocktest_flow_testLine269[0] = true;
            }
        } finally {
            for (int _blocktest_testLine269_i = 0; _blocktest_testLine269_i < 1; _blocktest_testLine269_i += 1) {
                assertTrue(_blocktest_flow_testLine269[_blocktest_testLine269_i]);
            }
        }
    }

    @Test
    public void testLine274() throws Exception {
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
        boolean[] _blocktest_flow_testLine274 = new boolean[1];
        try {
            if (patchPositionByFile.get(revision).entrySet().stream().anyMatch(v -> (1 == 1) && v.getValue().contains(line))) {
                GitLabApiV4Wrapper.LOG.debug("getRevisionForLine found {}");
                {
                    assertEquals("NOT_MATCH", (revision));
                    return;
                }
            } else {
                _blocktest_flow_testLine274[0] = true;
            }
        } finally {
            for (int _blocktest_testLine274_i = 0; _blocktest_testLine274_i < 1; _blocktest_testLine274_i += 1) {
                assertTrue(_blocktest_flow_testLine274[_blocktest_testLine274_i]);
            }
        }
    }
}
