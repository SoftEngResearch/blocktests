package com.vizor.unreal.convert;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.vizor.unreal.tree.CppArgument;
import com.vizor.unreal.tree.CppField;
import com.vizor.unreal.tree.CppFunction;
import com.vizor.unreal.tree.CppNamespace;
import com.vizor.unreal.tree.CppStruct;
import com.vizor.unreal.tree.CppType;
import com.vizor.unreal.util.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import com.vizor.unreal.util.TriFunction;
import static com.vizor.unreal.tree.CppRecord.Residence.Header;
import static com.vizor.unreal.tree.CppType.Kind.Enum;
import static com.vizor.unreal.tree.CppType.Kind.Struct;
import static java.lang.System.lineSeparator;
import static java.text.MessageFormat.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.vizor.unreal.convert.CastGenerator.*;

public class CastGeneratorBlockTest {

    @Test
    public void testLine249() throws Exception {
        StringBuilder switchBody = new StringBuilder();
        AtomicInteger index = new AtomicInteger(0);
        CppType param = CppType.plain("foo", CppType.Kind.Primitive);
        CppField inField;
        switchBody.append(String.format("\tcase %d:", index.get())).append(lineSeparator());
        if (true) {
            switchBody.append(String.format("\t\t%s.set_allocated_%s(new %s(Proto_Cast<%s>(%s.%s.Get<%s>())));", CastGenerator.outputItemName, param.getVariantName(), param.toString(), param.toString(), CastGenerator.inputItemName, "fieldName", "params")).append(lineSeparator());
        } else {
            switchBody.append(String.format("\t\t%s.set_%s(Proto_Cast<%s>(%s.%s.Get<%s>()));", CastGenerator.outputItemName, param.getVariantName(), param.toString(), CastGenerator.inputItemName, "fieldName", "params")).append(lineSeparator());
        }
        switchBody.append("\t\tbreak;").append(lineSeparator());
        System.out.println(switchBody);
        assertEquals("\tcase 0:\n\t\tOutItem.set_allocated_null(new foo(Proto_Cast<foo>(InItem.fieldName.Get<params>())));\n\t\tbreak;\n", switchBody.toString());
    }

    @Test
    public void testLine254() throws Exception {
        StringBuilder switchBody = new StringBuilder();
        AtomicInteger index = new AtomicInteger(0);
        CppType param = CppType.plain("foo", CppType.Kind.Primitive);
        CppField inField;
        switchBody.append(String.format("\tcase %d:", index.get())).append(lineSeparator());
        if (false) {
            switchBody.append(String.format("\t\t%s.set_allocated_%s(new %s(Proto_Cast<%s>(%s.%s.Get<%s>())));", CastGenerator.outputItemName, param.getVariantName(), param.toString(), param.toString(), CastGenerator.inputItemName, "fieldName", "params")).append(lineSeparator());
        } else {
            switchBody.append(String.format("\t\t%s.set_%s(Proto_Cast<%s>(%s.%s.Get<%s>()));", CastGenerator.outputItemName, param.getVariantName(), param.toString(), CastGenerator.inputItemName, "fieldName", "params")).append(lineSeparator());
        }
        switchBody.append("\t\tbreak;").append(lineSeparator());
        System.out.println(switchBody);
        assertEquals("\tcase 0:\n\t\tOutItem.set_null(Proto_Cast<foo>(InItem.fieldName.Get<params>()));\n\t\tbreak;\n", switchBody.toString());
    }
}
