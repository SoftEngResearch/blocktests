package io.d2a.eeee;

import io.d2a.eeee.inject.Injector;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import io.d2a.eeee.annotation.annotations.prompt.Entrypoint;
import io.d2a.eeee.annotation.provider.PriorityAnnotationProvider;
import io.d2a.eeee.converter.StringConverter;
import io.d2a.eeee.prompt.Call;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;
import static io.d2a.eeee.EntryMethod.*;

public class EntryMethodBlockTest {

    @Test
    public void testLine66() throws Exception {
        EntryPointCollection epc = new EntryPointCollection(new Scanner(System.in), new Injector(), new ArrayList<>());
        EntryMethod targetMethod = new EntryMethod(String.class.getMethods()[0], String.class.getMethods()[0].getAnnotation(Entrypoint.class), String.class);
        try {
            try {
                {
                    assertTrue((epc.invoke(targetMethod)) == null);
                    return;
                }
            } catch (Exception e) {
                {
                    assertTrue((null) == null);
                    return;
                }
            }
        } finally {
        }
    }
}
