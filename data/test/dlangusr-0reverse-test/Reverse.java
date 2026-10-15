// FRAMEWORK: junit4

package com.example;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class Reverse {
    public static <T> void reverse(List<T> list) {
    // @blocktest().configParam("test_inputs", "int int int int", new Object[][] {{1, 0, 7, 6}, {0, 1, 6, 6}});
        int left = 0;
        int right = list.size() - 1;

        while (left < right) {
            T temp = list.get(left); // Cannot be inside block because of generics limitations
            // The block test does not compile because `list` is an interface as is and cannot be passed to function
            // This is why we need the block test to be in a comment

            /* @blocktest().given(list, new ArrayList<Integer>(), "List<Integer>") 
                        .parameterized("test_inputs", right, left, expected0, expected1)
                        .assume(expected0 != expected1)
                        .given(temp, 6, "Integer")
                        .setup(() -> { 
                            list.add(6);
                            list.add(7); 
                        })
                       .checkEq(list.get(0), Integer.valueOf(expected0))
                       .checkEq(list.get(1), Integer.valueOf(expected1));
            */    
            // { Add BlockTest - check swapped values               
            list.set(left, list.get(right));
            list.set(right, temp);
            // }

            left++;
            right--;
        }
    }
}
            /* blocktest().given(list, new ArrayList<Integer>(), "List<Integer>") 
                        .given(right, 1)
                        .given(left, 0)
                        .given(temp, 6, "Integer")
                        .setup(() -> { 
                            list.add(6);
                            list.add(7); 
                        })
                       .checkEq(list.get(0), Integer.valueOf(7))
                       .checkEq(list.get(1), Integer.valueOf(6));
            */    