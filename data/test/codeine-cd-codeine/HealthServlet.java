// TEST_DIR: test/codeine/servlet
package codeine.servlet;

import codeine.utils.exceptions.FileReadWriteException;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.types.Flow.IfStmt;
import static org.blocktest.utils.Constant.*;

import codeine.servlets.ReloadConfigurationServlet;
import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.health.HealthCheckRegistry;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class HealthServlet extends AbstractApiServlet {

    private static final Logger log = Logger.getLogger(HealthServlet.class);
    private final HealthCheckRegistry healthCheckRegistry;

    @Inject
    public HealthServlet(HealthCheckRegistry healthCheckRegistry) {
        this.healthCheckRegistry = healthCheckRegistry;
    }

    @Override
    public void myGet(HttpServletRequest request, HttpServletResponse response) {
        SortedMap<String, Result> res = healthCheckRegistry.runHealthChecks();
        List<Entry<String, Result>> failedHealthChecks = res.entrySet().stream()
            .filter(stringResultEntry -> !stringResultEntry.getValue().isHealthy()).collect(Collectors.toList());
        if (!failedHealthChecks.isEmpty()) {
            failedHealthChecks.forEach(stringResultEntry -> {
                // BLOCKTEST EVAL: https://github.com/codeine-cd/codeine/blob/355463b17c671925bc8bd3951dabb98a150dfe36/src/common/codeine/servlet/HealthServlet.java#L31-L38
                blocktest().given(stringResultEntry, new java.util.HashMap<String, Result>(){{put("a", Result.healthy());}}.entrySet().iterator().next())
                        .mock("log.warn(..)").mock("log.error(..)").checkFlow(IfStmt().Else());
                blocktest().given(stringResultEntry, new java.util.HashMap<String, Result>(){{put("a", Result.unhealthy("bad bad"));}}.entrySet().iterator().next())
                        .mock("log.warn(..)").mock("log.error(..)").checkFlow(IfStmt().Else());
                blocktest().given(stringResultEntry, new java.util.HashMap<String, Result>(){{put("a", Result.unhealthy(new FileNotFoundException()));}}.entrySet().iterator().next())
                        .mock("log.warn(..)").mock("log.error(..)").checkFlow(IfStmt().Then());
                log.warn("Failed health check " + stringResultEntry.getKey());
                log.warn("Failed health check " + stringResultEntry.getValue().getMessage());
                Throwable error = stringResultEntry.getValue().getError();
                if (error != null) {
                    log.error("Error is", error);
                } else {}
            });
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            log.info("Responded to health check: " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            log.info("Responded to health check: " + HttpServletResponse.SC_OK);
        }
        writeResponseJson(response, res);
    }

    @Override
    protected boolean checkPermissions(HttpServletRequest request) {
        return true;
    }

}
