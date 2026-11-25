package sample;

import static org.blocktest.BTest.blocktest;

public class Sample {
    public void checkVariable(boolean useLong) {
        Number value = null;
        blocktest("matchingVariableType").given(useLong, false).checkType(value, Integer.class);
        blocktest("wrongVariableType").given(useLong, true).checkType(value, Integer.class);
        if (useLong) {
            value = 7L;
        } else {
            value = 7;
        }
    }

    public Number checkReturn(boolean useLong) {
        blocktest("matchingReturnType").given(useLong, false).checkReturnType(Integer.class);
        blocktest("wrongReturnType").given(useLong, true).checkReturnType(Integer.class);
        if (useLong) {
            return 7L;
        }
        return 7;
    }
}
