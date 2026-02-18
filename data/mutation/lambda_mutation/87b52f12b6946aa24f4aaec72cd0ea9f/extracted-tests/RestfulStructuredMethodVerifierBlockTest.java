package org.lastaflute.web.path.restful.verifier;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.dbflute.helper.message.ExceptionMessageBuilder;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.Srl;
import org.lastaflute.di.util.tiger.LdiGenericUtil;
import org.lastaflute.web.exception.ExecuteMethodIllegalDefinitionException;
import org.lastaflute.web.path.restful.analyzer.RestfulComponentAnalyzer;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.ruts.config.ActionExecute;
import org.lastaflute.web.ruts.config.ActionFormMeta;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.lastaflute.web.path.restful.verifier.RestfulStructuredMethodVerifier.*;

public class RestfulStructuredMethodVerifierBlockTest {

    @Test
    public void testLine429() throws Exception {
        int eventSuffixParamCount = 1;
        try {
            {
                assertTrue((eventSuffixParamCount < 1));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine430() throws Exception {
        int eventSuffixParamCount = 1;
        try {
            {
                assertFalse((eventSuffixParamCount < 2));
                return;
            }
        } finally {
        }
    }
}
