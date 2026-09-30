// FRAMEWORK: jqwik
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
        int left = 0;
        int right = list.size() - 1;

        while (left < right) {
            T temp = list.get(left); // Cannot be inside block because of generics limitations

            /* @propblocktest().given(list, new ArrayList<Integer>(), "List<Integer>")
                       .given(left, 0).given(right, 1).given(temp, 6, "Integer").setup(() -> { 
                            list.add(6);
                            list.add(7); 
                        })
                       .checkEq(list.get(0), Integer.valueOf(7))
                       .checkEq(list.get(1), Integer.valueOf(6));
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
