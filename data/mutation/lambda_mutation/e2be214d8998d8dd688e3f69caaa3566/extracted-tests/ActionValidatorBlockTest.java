package org.lastaflute.web.validation;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import javax.validation.metadata.ConstraintDescriptor;
import org.dbflute.helper.message.ExceptionMessageBuilder;
import org.dbflute.jdbc.Classification;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfTypeUtil;
import org.dbflute.util.Srl;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.lastaflute.core.magic.ThreadCacheContext;
import org.lastaflute.core.message.MessageManager;
import org.lastaflute.core.message.UserMessage;
import org.lastaflute.core.message.UserMessages;
import org.lastaflute.core.message.supplier.MessageLocaleProvider;
import org.lastaflute.core.message.supplier.UserMessagesCreator;
import org.lastaflute.di.helper.beans.BeanDesc;
import org.lastaflute.di.helper.beans.PropertyDesc;
import org.lastaflute.di.helper.beans.factory.BeanDescFactory;
import org.lastaflute.web.api.ApiFailureResource;
import org.lastaflute.web.path.ActionAdjustmentProvider;
import org.lastaflute.web.response.ApiResponse;
import org.lastaflute.web.ruts.process.ActionRuntime;
import org.lastaflute.web.servlet.request.RequestManager;
import org.lastaflute.web.util.LaActionRuntimeUtil;
import org.lastaflute.web.validation.exception.ClientErrorByValidatorException;
import org.lastaflute.web.validation.exception.ValidationErrorException;
import org.lastaflute.web.validation.exception.ValidationStoppedException;
import org.lastaflute.web.validation.theme.conversion.TypeFailureBean;
import org.lastaflute.web.validation.theme.conversion.TypeFailureElement;
import org.lastaflute.web.validation.theme.conversion.ValidateTypeFailure;
import org.lastaflute.web.validation.theme.typed.TypeBigDecimal;
import org.lastaflute.web.validation.theme.typed.TypeBoolean;
import org.lastaflute.web.validation.theme.typed.TypeDouble;
import org.lastaflute.web.validation.theme.typed.TypeFloat;
import org.lastaflute.web.validation.theme.typed.TypeInteger;
import org.lastaflute.web.validation.theme.typed.TypeLocalDate;
import org.lastaflute.web.validation.theme.typed.TypeLocalDateTime;
import org.lastaflute.web.validation.theme.typed.TypeLocalTime;
import org.lastaflute.web.validation.theme.typed.TypeLong;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.lastaflute.web.validation.ActionValidator.*;

public class ActionValidatorBlockTest {

    @Test
    public void a() throws Exception {
        java.lang.annotation.Annotation anno = ActionValidator.class.getDeclaredField("hibernateValidator").getAnnotation(NotNull.class);
        try {
            {
                assertFalse(((0 == 1) || anno instanceof Required));
                return;
            }
        } finally {
        }
    }

    @Test
    public void b() throws Exception {
        java.lang.annotation.Annotation anno = ActionValidator.class.getDeclaredField("blocktestNotNull").getAnnotation(NotNull.class);
        try {
            {
                assertTrue(((0 == 1) || anno instanceof Required));
                return;
            }
        } finally {
        }
    }

    @Test
    public void c() throws Exception {
        java.lang.annotation.Annotation anno = ActionValidator.class.getDeclaredField("blocktestRequired").getAnnotation(Required.class);
        try {
            {
                assertTrue(((0 == 1) || anno instanceof Required));
                return;
            }
        } finally {
        }
    }
}
