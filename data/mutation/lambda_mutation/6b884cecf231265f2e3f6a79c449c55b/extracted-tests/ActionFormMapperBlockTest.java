package org.lastaflute.web.ruts.process;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.dbflute.jdbc.Classification;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfCollectionUtil;
import org.dbflute.util.DfTypeUtil;
import org.lastaflute.core.direction.FwAssistantDirector;
import org.lastaflute.core.json.JsonObjectConvertible;
import org.lastaflute.core.magic.ThreadCacheContext;
import org.lastaflute.core.util.ContainerUtil;
import org.lastaflute.di.helper.beans.BeanDesc;
import org.lastaflute.di.helper.beans.ParameterizedClassDesc;
import org.lastaflute.di.helper.beans.PropertyDesc;
import org.lastaflute.di.helper.beans.factory.BeanDescFactory;
import org.lastaflute.di.util.LdiArrayUtil;
import org.lastaflute.di.util.LdiClassUtil;
import org.lastaflute.di.util.LdiModifierUtil;
import org.lastaflute.web.LastaWebKey;
import org.lastaflute.web.api.JsonParameter;
import org.lastaflute.web.direction.FwWebDirection;
import org.lastaflute.web.path.ActionAdjustmentProvider;
import org.lastaflute.web.path.FormMappingOption;
import org.lastaflute.web.ruts.VirtualForm;
import org.lastaflute.web.ruts.config.ActionFormMeta;
import org.lastaflute.web.ruts.inoutlogging.InOutLogKeeper;
import org.lastaflute.web.ruts.multipart.MultipartRequestHandler;
import org.lastaflute.web.ruts.multipart.MultipartRequestWrapper;
import org.lastaflute.web.ruts.multipart.MultipartResourceProvider;
import org.lastaflute.web.ruts.process.formcoins.FormCoinsHelper;
import org.lastaflute.web.ruts.process.populate.FormSimpleTextParameterFilter;
import org.lastaflute.web.ruts.process.populate.FormSimpleTextParameterMeta;
import org.lastaflute.web.ruts.process.populate.FormYourCollectionResource;
import org.lastaflute.web.servlet.filter.RequestLoggingFilter.WholeShowErrorFlushAttribute;
import org.lastaflute.web.servlet.request.RequestManager;
import org.lastaflute.web.validation.theme.conversion.TypeFailureBean;
import org.lastaflute.web.validation.theme.conversion.TypeFailureElement;
import org.lastaflute.web.validation.theme.conversion.ValidateTypeFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.lastaflute.web.ruts.process.ActionFormMapper.*;

public class ActionFormMapperBlockTest {

    @Test
    public void testLine935() throws Exception {
        FormSimpleTextParameterFilter filter = (FormSimpleTextParameterFilter) (str, meta) -> str.replace(meta.getPropertyName(), "BANNED");
        String propertyName = "foo";
        Class<?> propertyType = String.class;
        String[] ary = new String[] { "foo1", "bar2", "baz3", null, "the word foo is BANNED" };
        FormSimpleTextParameterMeta meta = new FormSimpleTextParameterMeta(propertyName, propertyType);
        int index = 0;
        for (String element : ary) {
            if (element != null) {
                // just in case
                ary[index] = filter.filter(element, meta);
            }
        }
        assertEquals("the word BANNED is BANNED", ary[4]);
        assertEquals(null, ary[3]);
        assertEquals("baz3", ary[2]);
        assertEquals("bar2", ary[1]);
        assertEquals("BANNED1", ary[0]);
    }
}
