package toy;

import static org.blocktest.BTest.propblocktest;
import static org.blocktest.types.EndAt.FIRST_BLOCK;
import static org.blocktest.types.Flow.IfStmt;

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
//            /* @propblocktest().givenAll(list, "List<Integer>") 
//                        .givenAll(left, "int")
//                        .givenAll(right, "int")
//                        .given(temp, 0, "int")
//                        .setup(() -> { 
//                            int original_list_size = list.size();
//                            temp = list.get(left);
//                        })
//                        .checkEq(original_list_size, list.size()).end(FIRST_BLOCK);
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
//            /* @propblocktest().givenAll(list, "List<Integer>") 
//                        .givenAll(left, "int")
//                        .givenAll(right, "int")
//                        .given(temp, 0, "int")
//                        .assume(true)
//                        .setup(() -> { 
//                            Assume.that(list.size() > 1);
//                            Assume.that(left < list.size() && right < list.size() && left > 0 && right > 0);
//                            int original_list_size = list.size();
//                            temp = list.get(left);
//                        })
//                        .checkEq(original_list_size, list.size()).end(FIRST_BLOCK);
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
//            /* @propblocktest().givenAll(list, "List<Integer>") 
//                        .givenAll(left, "int")
//                        .givenAll(right, "int")
//                        .given(temp, 0, "int")
//                        .setup(() -> { 
//                            Assume.that(list.size() > 1);
//                            Assume.that(left < list.size() && right < list.size() && left > 0 && right > 0);
//                            int original_list_size = list.size();
//                            temp = list.get(left);
//                        })
//                        .assume(left + right == list.size() - 1)
//                        .checkEq(original_list_size, list.size()).end(FIRST_BLOCK);
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
