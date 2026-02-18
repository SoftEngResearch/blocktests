package com.jfinal.proxy;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.InterceptorManager;
import com.jfinal.kit.Kv;
import com.jfinal.log.Log;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.jfinal.proxy.ProxyGenerator.*;

public class ProxyGeneratorBlockTest {

    @Test
    public void testLine105() throws Exception {
        java.lang.reflect.Parameter x = Arrays.stream(Arrays.stream(com.jfinal.kit.Okv.class.getDeclaredMethods()).filter(z -> z.getName().equals("keep")).iterator().next().getParameters()).iterator().next();
        try {
            StringBuilder sb = new StringBuilder();
            Type type = x.getParameterizedType();
            String typename = type.getTypeName();
            if (x.isVarArgs()) {
                sb.append(typename.replaceFirst("\\[\\]$", "..."));
            } else {
            }
            {
                assertEquals("java.lang.String...", (sb.toString()));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine107() throws Exception {
        java.lang.reflect.Parameter x = Arrays.stream(Arrays.stream(ProxyGenerator.class.getDeclaredMethods()).filter(z -> z.getName().equals("isSkipMethod")).iterator().next().getParameters()).iterator().next();
        try {
            StringBuilder sb = new StringBuilder();
            Type type = x.getParameterizedType();
            String typename = type.getTypeName();
            if (x.isVarArgs()) {
                sb.append(typename.replaceFirst("\\[\\]$", "..."));
            } else {
            }
            {
                assertEquals("java.lang.reflect.Method", (sb.toString()));
                return;
            }
        } finally {
        }
    }
}
