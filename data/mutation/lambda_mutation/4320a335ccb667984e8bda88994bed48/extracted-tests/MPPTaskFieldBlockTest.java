package net.sf.mpxj.common;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.util.Arrays;
import net.sf.mpxj.DataType;
import net.sf.mpxj.FieldType;
import net.sf.mpxj.FieldTypeClass;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.TaskField;
import net.sf.mpxj.UserDefinedField;
import org.junit.Test;
import static org.junit.Assert.*;
import static net.sf.mpxj.common.MPPTaskField.*;

public class MPPTaskFieldBlockTest {

    @Test
    public void testLine61() throws Exception {
        int k = 0xFFF;
        try {
            int id = (k.intValue() & 0xFFF) + 1;
            String internalName = "ENTERPRISE_CUSTOM_FIELD" + id;
            String externalName = "Enterprise Custom Field " + id;
            {
                assertTrue((new UserDefinedField(k, externalName, internalName, FieldTypeClass.TASK, false, DataType.CUSTOM)).toString().equals(new UserDefinedField(0xFFF, "ENTERPRISE_CUSTOM_FIELD4096", "Enterprise Custom Field 4096", FieldTypeClass.TASK, false, DataType.CUSTOM).toString()));
                return;
            }
        } finally {
        }
    }
}
