// FRAMEWORK: junit4

package toy;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import static org.blocktest.types.EndAt.FIRST_BLOCK;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//public class ReverseList {
//    public static <T> void reverse(List<T> list) {
//        int left = 0;
//        int right = list.size() - 1;
//
//        while (left < right) {
//            T temp = list.get(left); // Cannot be inside block because of generics limitations
//            // note how we reinitilize it and set it to zero just to give it a type using `given` 
//
//            // The block test does not compile because `list` is an interface as is and cannot be passed to function
//            // whose argument does not implement `List`
//            // This is why we need the block test to be in a comment
//
//            /* @blocktest().given(list, Arrays.asList(6, 7), "List<Integer>")
//                        .given(left, 0)
//                        .given(right, 1)
//                        .given(temp, 0, "Integer")
//                        .setup(() -> { 
//                            temp = list.get(left);
//                        })
//                        .checkEq(list.get(right), Integer.valueOf(6))
//                        .checkEq(list.get(left), Integer.valueOf(7)).end(FIRST_BLOCK);
//            */    
//            // { Add BlockTest - check swapped values 
//            {
//            list.set(left, list.get(right));
//            list.set(right, temp);
//            }
//            // }
//
//            left++;
//            right--;
//        }
//    }
//}


//public class ReverseList {
//    public static <T> void reverse(List<T> list) {
//        int left = 0;
//        int right = list.size() - 1;
//
//        while (left < right) {
//            T temp = list.get(left); // Cannot be inside block because of generics limitations
//            // note how we reinitilize it and set it to zero just to give it a type using `given` 
//
//            // The block test does not compile because `list` is an interface as is and cannot be passed to function
//            // whose argument does not implement `List`
//            // This is why we need the block test to be in a comment
//
//            /* @blocktest().parameterized(new Object[][] {{Arrays.asList(6, 7), 0, 1, 7, 6},
//                                                       {Arrays.asList(6, 7, 8), 0, 2, 8, 6},
//                                                       {Arrays.asList(2, 6, 7, 4), 0, 3, 4, 2}},
//                            "List<Integer> int int int int", 
//                            list, left, right, expected0, expected1)
//                        .given(temp, 0, "Integer")
//                        .setup(() -> { 
//                            temp = list.get(left);
//                        })
//                        .checkEq(list.get(right), Integer.valueOf(expected1))
//                        .checkEq(list.get(left), Integer.valueOf(expected0)).end(FIRST_BLOCK);
//            */    
//            // { Add BlockTest - check swapped values 
//            {
//            list.set(left, list.get(right));
//            list.set(right, temp);
//            }
//            // }
//
//            left++;
//            right--;
//        }
//    }
//}

//public class ReverseList {
//    public static <T> void reverse(List<T> list) {
//        int left = 0;
//        int right = list.size() - 1;
//
//        while (left < right) {
//            T temp = list.get(left); // Cannot be inside block because of generics limitations
//            // note how we reinitilize it and set it to zero just to give it a type using `given` 
//
//            // The block test does not compile because `list` is an interface as is and cannot be passed to function
//            // whose argument does not implement `List`
//            // This is why we need the block test to be in a comment
//
//            /* @blocktest().parameterized(new Object[][] {{Arrays.asList(6, 7), 0, 1, 7, 6},
//                                                       {Arrays.asList(6, 7, 8), 0, 2, 8, 6},
//                                                       {Arrays.asList(2, 6, 7, 4), 0, 3, 4, 2}},
//                            "List<Integer> int int int int", 
//                            list, left, right, expected0, expected1)
//                        .given(temp, 0, "Integer")
//                        .setup(() -> { 
//                            temp = list.get(left);
//                        })
//                        .assume(list.size() <= 2)
//                        .checkEq(list.get(right), Integer.valueOf(expected1))
//                        .checkEq(list.get(left), Integer.valueOf(expected0)).end(FIRST_BLOCK);
//            */    
//            // { Add BlockTest - check swapped values 
//            {
//            list.set(left, list.get(right));
//            list.set(right, temp);
//            }
//            // }
//
//            left++;
//            right--;
//        }
//    }
//}

//public class ReverseList {
//    public static <T> void reverse(List<T> list) {
//        /*  @blocktest().configParam("test_set", "List<Integer> int int int int",
//                                        new Object[][] {{Arrays.asList(6, 7), 0, 1, 7, 6},
//                                                        {Arrays.asList(6, 7, 8), 0, 2, 8, 6},
//                                                        {Arrays.asList(2, 6, 7, 4), 0, 3, 4, 2}})
//        */
//        int left = 0;
//        int right = list.size() - 1;
//
//        while (left < right) {
//            T temp = list.get(left); // Cannot be inside block because of generics limitations
//            // note how we reinitilize it and set it to zero just to give it a type using `given` 
//
//            // The block test does not compile because `list` is an interface as is and cannot be passed to function
//            // whose argument does not implement `List`
//            // This is why we need the block test to be in a comment
//
//            /* @blocktest().parameterized("test_set", list, left, right, expected0, expected1)
//                        .given(temp, 0, "Integer")
//                        .setup(() -> { 
//                            temp = list.get(left);
//                        })
//                        .assume(list.size() <= 3)
//                        .checkEq(list.get(right), Integer.valueOf(expected1))
//                        .checkEq(list.get(left), Integer.valueOf(expected0)).end(FIRST_BLOCK);
//            */    
//            // { Add BlockTest - check swapped values 
//            {
//            list.set(left, list.get(right));
//            list.set(right, temp);
//            }
//            // }
//
//            left++;
//            right--;
//        }
//    }
//}