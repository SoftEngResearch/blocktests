package com.vizor.unreal.preprocess;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.squareup.wire.schema.internal.parser.EnumElement;
import com.squareup.wire.schema.internal.parser.FieldElement;
import com.squareup.wire.schema.internal.parser.MessageElement;
import com.squareup.wire.schema.internal.parser.ProtoFileElement;
import com.squareup.wire.schema.internal.parser.TypeElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.squareup.wire.schema.internal.parser.ProtoFileElement.builder;
import static java.util.stream.Collectors.toList;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.vizor.unreal.preprocess.NestedTypesRemover.*;

public class NestedTypesRemoverBlockTest {

    @Test
    public void line1() throws Exception {
        FieldElement f = FieldElement.builder(com.squareup.wire.schema.Location.get("foo")).type("foo").tag(1).name("xxx").build();
        Map<String, String> renames = new HashMap<String, String>() {

            {
                put("foo", "bar");
            }
        };
        try {
            if (renames.containsKey(f.type())) {
                assertEquals("xxx", (FieldElement.builder(f.location()).type(renames.get(f.type())).tag(f.tag()).options(f.options()).name(f.name()).location(f.location()).label(f.label()).documentation(f.documentation())./*.defaultValue(f.defaultValue())*/
                build()).name());
                assertEquals("bar", (FieldElement.builder(f.location()).type(renames.get(f.type())).tag(f.tag()).options(f.options()).name(f.name()).location(f.location()).label(f.label()).documentation(f.documentation())./*.defaultValue(f.defaultValue())*/
                build()).type());
                return;
            }
            {
                assertEquals("xxx", (f).name());
                assertEquals("bar", (f).type());
                return;
            }
        } finally {
        }
    }

    @Test
    public void line2() throws Exception {
        FieldElement f = FieldElement.builder(com.squareup.wire.schema.Location.get("foo")).type("foo").tag(1).name("xxx").build();
        Map<String, String> renames = new HashMap<String, String>() {

            {
                put("foo2", "bar");
            }
        };
        try {
            if (renames.containsKey(f.type())) {
                assertEquals("xxx", (FieldElement.builder(f.location()).type(renames.get(f.type())).tag(f.tag()).options(f.options()).name(f.name()).location(f.location()).label(f.label()).documentation(f.documentation())./*.defaultValue(f.defaultValue())*/
                build()).name());
                assertEquals("foo", (FieldElement.builder(f.location()).type(renames.get(f.type())).tag(f.tag()).options(f.options()).name(f.name()).location(f.location()).label(f.label()).documentation(f.documentation())./*.defaultValue(f.defaultValue())*/
                build()).type());
                return;
            }
            {
                assertEquals("xxx", (f).name());
                assertEquals("foo", (f).type());
                return;
            }
        } finally {
        }
    }
}
