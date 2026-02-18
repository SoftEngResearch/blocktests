package org.lastaflute.web.ruts;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.dbflute.helper.message.ExceptionMessageBuilder;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.core.magic.ThreadCacheContext;
import org.lastaflute.core.message.UserMessage;
import org.lastaflute.core.message.UserMessages;
import org.lastaflute.db.jta.romanticist.SavedTransactionMemories;
import org.lastaflute.db.jta.romanticist.TransactionMemoriesProvider;
import org.lastaflute.db.jta.stage.BegunTx;
import org.lastaflute.db.jta.stage.TransactionGenre;
import org.lastaflute.db.jta.stage.TransactionStage;
import org.lastaflute.web.LastaWebKey;
import org.lastaflute.web.exception.ActionWrappedCheckedException;
import org.lastaflute.web.exception.ExecuteMethodAccessFailureException;
import org.lastaflute.web.exception.ExecuteMethodArgumentMismatchException;
import org.lastaflute.web.hook.ActionHook;
import org.lastaflute.web.response.ActionResponse;
import org.lastaflute.web.ruts.config.ActionExecute;
import org.lastaflute.web.ruts.process.ActionResponseReflector;
import org.lastaflute.web.ruts.process.ActionRuntime;
import org.lastaflute.web.ruts.process.exception.ActionCreateFailureException;
import org.lastaflute.web.servlet.filter.RequestLoggingFilter.WholeShowAttribute;
import org.lastaflute.web.servlet.request.RequestManager;
import org.lastaflute.web.util.LaActionExecuteUtil;
import org.lastaflute.web.validation.VaErrorHook;
import org.lastaflute.web.validation.exception.ValidationErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.lastaflute.web.ruts.GodHandableAction.*;

public class GodHandableActionBlockTest {

    @Test
    public void testLine424() throws Exception {
        StringBuilder sb = new StringBuilder();
        java.lang.String GodHandableAction__LF = "\n";
        java.lang.String property = "property";
        UserMessages messages = new UserMessages();
        sb.append(GodHandableAction__LF).append(" ").append(property);
        assertEquals("\n property", sb.toString());
    }
}
