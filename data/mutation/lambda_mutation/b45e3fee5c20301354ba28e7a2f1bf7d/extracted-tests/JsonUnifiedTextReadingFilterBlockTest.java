package org.lastaflute.core.json.filter;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.lastaflute.core.json.control.JsonMappingControlMeta;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.lastaflute.core.json.filter.JsonUnifiedTextReadingFilter.*;

public class JsonUnifiedTextReadingFilterBlockTest {

    @Test
    public void testLine51() throws Exception {
        JsonSimpleTextReadingFilter simple = (JsonSimpleTextReadingFilter) text1 -> text1.substring(2);
        JsonTypeableTextReadingFilter typeable = (JsonTypeableTextReadingFilter) (type, text1) -> text1.substring(4);
        java.lang.Class adaptingType = String.class;
        java.lang.String text = "ABCDEFGH";
        try {
            String filtered = text;
            if (simple != null) {
                filtered = simple.filter(filtered);
            }
            if (typeable != null) {
            }
            {
                assertEquals("GH", (filtered));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine55() throws Exception {
        JsonSimpleTextReadingFilter simple = (JsonSimpleTextReadingFilter) text1 -> text1.substring(2);
        JsonTypeableTextReadingFilter typeable = (JsonTypeableTextReadingFilter) (type, text1) -> type.getName().contains("String") ? text1.substring(1) : text1.substring(4);
        java.lang.Class adaptingType = String.class;
        java.lang.String text = "ABCDEFGH";
        try {
            String filtered = text;
            if (simple != null) {
                filtered = simple.filter(filtered);
            }
            if (typeable != null) {
            }
            {
                assertEquals("DEFGH", (filtered));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine59() throws Exception {
        JsonSimpleTextReadingFilter simple = (JsonSimpleTextReadingFilter) text1 -> text1.substring(2);
        JsonTypeableTextReadingFilter typeable = (JsonTypeableTextReadingFilter) (type, text1) -> type.getName().contains("String") ? text1.substring(1) : text1.substring(4);
        java.lang.Class adaptingType = Integer.class;
        java.lang.String text = "ABCDEFGH";
        try {
            String filtered = text;
            if (simple != null) {
                filtered = simple.filter(filtered);
            }
            if (typeable != null) {
            }
            {
                assertEquals("GH", (filtered));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine63() throws Exception {
        JsonSimpleTextReadingFilter simple = (JsonSimpleTextReadingFilter) text1 -> text1.substring(2);
        JsonTypeableTextReadingFilter typeable = null;
        java.lang.Class adaptingType = String.class;
        java.lang.String text = "ABCDEFGH";
        try {
            String filtered = text;
            if (simple != null) {
                filtered = simple.filter(filtered);
            }
            if (typeable != null) {
            }
            {
                assertEquals("CDEFGH", (filtered));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine67() throws Exception {
        JsonSimpleTextReadingFilter simple = null;
        JsonTypeableTextReadingFilter typeable = (JsonTypeableTextReadingFilter) (type, text1) -> text1.substring(4);
        java.lang.Class adaptingType = String.class;
        java.lang.String text = "ABCDEFGH";
        try {
            String filtered = text;
            if (simple != null) {
                filtered = simple.filter(filtered);
            }
            if (typeable != null) {
            }
            {
                assertEquals("EFGH", (filtered));
                return;
            }
        } finally {
        }
    }
}
