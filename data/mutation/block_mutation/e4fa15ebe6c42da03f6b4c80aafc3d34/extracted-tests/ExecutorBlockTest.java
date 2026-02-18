package com.wenshuo.agent.javassist.bytecode.analysis;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.wenshuo.agent.javassist.ClassPool;
import com.wenshuo.agent.javassist.CtClass;
import com.wenshuo.agent.javassist.NotFoundException;
import com.wenshuo.agent.javassist.bytecode.BadBytecode;
import com.wenshuo.agent.javassist.bytecode.CodeIterator;
import com.wenshuo.agent.javassist.bytecode.ConstPool;
import com.wenshuo.agent.javassist.bytecode.Descriptor;
import com.wenshuo.agent.javassist.bytecode.MethodInfo;
import com.wenshuo.agent.javassist.bytecode.Opcode;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.wenshuo.agent.javassist.bytecode.analysis.Executor.*;

public class ExecutorBlockTest {

    @Test
    public void testLine288() throws Exception {
        Frame frame = new Frame(1000, 1000);
        int DUP2_X1 = 93;
        int opcode = 0;
        for (int i = 0; i < 500; i++) {
            frame.push(Type.DOUBLE);
        }
        int end = frame.getTopIndex();
        int insert = end - (opcode - DUP2_X1) - 1;
        Type type1 = frame.getStack(frame.getTopIndex() - 1);
        Type type2 = frame.peek();
        frame.push(type1);
        frame.push(type2);
        while (end > insert) {
            frame.setStack(end, frame.getStack(end - 2));
            end--;
        }
        frame.setStack(insert, type2);
        assertEquals(Type.DOUBLE, frame.pop());
    }
}
