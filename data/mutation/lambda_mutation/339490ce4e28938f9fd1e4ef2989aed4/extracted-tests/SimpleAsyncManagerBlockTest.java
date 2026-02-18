package org.lastaflute.core.magic.async;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.dbflute.bhv.core.BehaviorCommandHook;
import org.dbflute.bhv.proposal.callback.ExecutedSqlCounter;
import org.dbflute.helper.message.ExceptionMessageBuilder;
import org.dbflute.hook.AccessContext;
import org.dbflute.hook.AccessContext.AccessModuleProvider;
import org.dbflute.hook.AccessContext.AccessProcessProvider;
import org.dbflute.hook.AccessContext.AccessUserProvider;
import org.dbflute.hook.CallbackContext;
import org.dbflute.hook.SqlFireHook;
import org.dbflute.hook.SqlLogHandler;
import org.dbflute.hook.SqlResultHandler;
import org.dbflute.hook.SqlStringFilter;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfReflectionUtil;
import org.dbflute.util.DfTraceViewUtil;
import org.dbflute.util.DfTypeUtil;
import org.dbflute.util.Srl;
import org.lastaflute.core.direction.FwAssistantDirector;
import org.lastaflute.core.direction.FwCoreDirection;
import org.lastaflute.core.exception.ExceptionTranslator;
import org.lastaflute.core.magic.ThreadCacheContext;
import org.lastaflute.core.magic.ThreadCompleted;
import org.lastaflute.core.magic.async.ConcurrentAsyncCall.ConcurrentAsyncImportance;
import org.lastaflute.core.magic.async.ConcurrentAsyncOption.ConcurrentAsyncInheritType;
import org.lastaflute.core.magic.async.bridge.AsyncStateBridge;
import org.lastaflute.core.magic.async.bridge.AsyncStateBridgeOpCall;
import org.lastaflute.core.magic.async.bridge.AsyncStateBridgeOption;
import org.lastaflute.core.magic.async.bridge.BridgeCallAdapter;
import org.lastaflute.core.magic.async.exception.ConcurrentParallelRunnerException;
import org.lastaflute.core.magic.async.future.BasicYourFuture;
import org.lastaflute.core.magic.async.future.DestructiveYourFuture;
import org.lastaflute.core.magic.async.future.YourFuture;
import org.lastaflute.core.magic.async.waiting.WaitingAsyncException;
import org.lastaflute.core.magic.async.waiting.WaitingAsyncResult;
import org.lastaflute.core.magic.destructive.BowgunDestructiveAdjuster;
import org.lastaflute.core.mail.PostedMailCounter;
import org.lastaflute.core.remoteapi.CalledRemoteApiCounter;
import org.lastaflute.db.dbflute.accesscontext.PreparedAccessContext;
import org.lastaflute.db.dbflute.callbackcontext.traceablesql.RomanticTraceableSqlFireHook;
import org.lastaflute.db.dbflute.callbackcontext.traceablesql.RomanticTraceableSqlResultHandler;
import org.lastaflute.db.dbflute.callbackcontext.traceablesql.RomanticTraceableSqlStringFilter;
import org.lastaflute.db.jta.romanticist.SavedTransactionMemories;
import org.lastaflute.db.jta.romanticist.TransactionMemoriesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.lastaflute.core.magic.async.SimpleAsyncManager.*;

public class SimpleAsyncManagerBlockTest {

    @Test
    public void testLine739() throws Exception {
        StringBuilder txSb = new StringBuilder();
        java.lang.String result = "100";
        if (txSb.length() == 0) {
            txSb.append(SimpleAsyncManager.LF).append(SimpleAsyncManager.EX_IND).append("; transactionMemories=wholeShow:");
        }
        txSb.append(Srl.indent(SimpleAsyncManager.EX_IND.length(), SimpleAsyncManager.LF + "*" + result));
        assertEquals("\n  ; transactionMemories=wholeShow:" + Srl.indent(SimpleAsyncManager.EX_IND.length(), SimpleAsyncManager.LF + "*" + "100"), txSb.toString());
    }

    @Test
    public void testLine740() throws Exception {
        StringBuilder txSb = new StringBuilder("hello");
        java.lang.String result = "100";
        if (txSb.length() == 0) {
            txSb.append(SimpleAsyncManager.LF).append(SimpleAsyncManager.EX_IND).append("; transactionMemories=wholeShow:");
        }
        txSb.append(Srl.indent(SimpleAsyncManager.EX_IND.length(), SimpleAsyncManager.LF + "*" + result));
        assertEquals("hello" + Srl.indent(SimpleAsyncManager.EX_IND.length(), SimpleAsyncManager.LF + "*" + "100"), txSb.toString());
    }
}
