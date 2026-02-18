/*
 * Copyright 2015-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.lastaflute.core.json.filter;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import org.lastaflute.core.json.control.JsonMappingControlMeta;

/**
 * The callback for filtering of typeable text parameter. <br>
 * (contains list elements and map keys/values)
 * @author jflute
 * @since 1.1.1 (2019/01/12 Saturday)
 */
@FunctionalInterface
public interface JsonUnifiedTextReadingFilter {

    /**
     * Filter the text parameter. (contains list elements and map keys/values) <br>
     * @param adaptingType The type of the adapter for the parameter (not field type). (NotNull)
     * @param text The text as JSON reading. (NotNull: not called if null parameter)
     * @return The filtered parameter or plain parameter. (NullAllowed: then filtered as null value)
     */
    String filter(Class<?> adaptingType, String text);

    // ===================================================================================
    //                                                                             Factory
    //                                                                             =======
    static JsonUnifiedTextReadingFilter unify(JsonMappingControlMeta controlMeta) { // null allowed
        final JsonSimpleTextReadingFilter simple = controlMeta.getSimpleTextReadingFilter().orElse(null);
        final JsonTypeableTextReadingFilter typeable = controlMeta.getTypeableTextReadingFilter().orElse(null);
        if (simple != null || typeable != null) {
            return (adaptingType, text) -> {
                // BLOCKTEST EVAL: https://github.com/lastaflute/lastaflute/blob/01201e2eee995053bf6f9febcf58ace01ed38c30/src/main/java/org/lastaflute/core/json/filter/JsonUnifiedTextReadingFilter.java#L44-L53
                blocktest().given(simple, (JsonSimpleTextReadingFilter) text1 -> text1.substring(2))
                        .given(typeable, (JsonTypeableTextReadingFilter) (type, text1) -> text1.substring(4))
                        .given(adaptingType, String.class).given(text, "ABCDEFGH").checkReturnEq("GH");

                blocktest().given(simple, (JsonSimpleTextReadingFilter) text1 -> text1.substring(2))
                        .given(typeable, (JsonTypeableTextReadingFilter) (type, text1) -> type.getName().contains("String") ? text1.substring(1) : text1.substring(4))
                        .given(adaptingType, String.class).given(text, "ABCDEFGH").checkReturnEq("DEFGH");

                blocktest().given(simple, (JsonSimpleTextReadingFilter) text1 -> text1.substring(2))
                        .given(typeable, (JsonTypeableTextReadingFilter) (type, text1) -> type.getName().contains("String") ? text1.substring(1) : text1.substring(4))
                        .given(adaptingType, Integer.class).given(text, "ABCDEFGH").checkReturnEq("GH");

                blocktest().given(simple, (JsonSimpleTextReadingFilter) text1 -> text1.substring(2))
                        .given(typeable, null)
                        .given(adaptingType, String.class).given(text, "ABCDEFGH").checkReturnEq("CDEFGH");

                blocktest().given(simple, null)
                        .given(typeable, (JsonTypeableTextReadingFilter) (type, text1) -> text1.substring(4))
                        .given(adaptingType, String.class).given(text, "ABCDEFGH").checkReturnEq("EFGH");
                
                String filtered = text;
                if (simple != null) {
                    filtered = simple.filter(filtered);
                }
                if (typeable != null) {
                    filtered = typeable.filter(adaptingType, filtered);
                }
                return filtered;
            };
        } else { // normally here
            return null;
        }
    }
}
