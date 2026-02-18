package com.siemens.ct.exi.core;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.util.HashSet;
import java.util.Set;
import com.siemens.ct.exi.core.exceptions.UnsupportedOption;
import com.siemens.ct.exi.core.grammars.event.EventType;
import com.siemens.ct.exi.core.grammars.grammar.Grammar;
import com.siemens.ct.exi.core.grammars.grammar.SchemaInformedFirstStartTagGrammar;
import com.siemens.ct.exi.core.grammars.grammar.SchemaInformedGrammar;
import com.siemens.ct.exi.core.grammars.grammar.SchemaInformedStartTagGrammar;
import com.siemens.ct.exi.core.util.MethodsBag;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.siemens.ct.exi.core.FidelityOptions.*;

public class FidelityOptionsBlockTest {

    @Test
    public void testLine402() throws Exception {
        int dec = 10;
        int ec2 = -3;
        EventType eventType = null;
        boolean isDTD = true;
        try {
            if (ec2 == 7 - dec) {
                // SE*
                eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
            } else if (ec2 == 8 - dec) {
                // CH
                eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
            } else {
                if (!isDTD) {
                    dec++;
                }
                if (ec2 == 9 - dec) {
                    // ER
                    eventType = EventType.ENTITY_REFERENCE;
                }
            }
            return;
        } finally {
            assertEquals(EventType.START_ELEMENT_GENERIC_UNDECLARED, eventType);
        }
    }

    @Test
    public void testLine405() throws Exception {
        int dec = 10;
        int ec2 = -2;
        EventType eventType = null;
        boolean isDTD = true;
        try {
            if (ec2 == 7 - dec) {
                // SE*
                eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
            } else if (ec2 == 8 - dec) {
                // CH
                eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
            } else {
                if (!isDTD) {
                    dec++;
                }
                if (ec2 == 9 - dec) {
                    // ER
                    eventType = EventType.ENTITY_REFERENCE;
                }
            }
            return;
        } finally {
            assertEquals(EventType.CHARACTERS_GENERIC_UNDECLARED, eventType);
        }
    }

    @Test
    public void testLine408() throws Exception {
        int dec = 10;
        int ec2 = 0;
        EventType eventType = null;
        boolean isDTD = false;
        if (ec2 == 7 - dec) {
            // SE*
            eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        } else if (ec2 == 8 - dec) {
            // CH
            eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (ec2 == 9 - dec) {
                // ER
                eventType = EventType.ENTITY_REFERENCE;
            }
        }
        assertEquals(11, dec);
    }

    @Test
    public void testLine411() throws Exception {
        int dec = 10;
        int ec2 = -1;
        EventType eventType = null;
        boolean isDTD = true;
        try {
            if (ec2 == 7 - dec) {
                // SE*
                eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
            } else if (ec2 == 8 - dec) {
                // CH
                eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
            } else {
                if (!isDTD) {
                    dec++;
                }
                if (ec2 == 9 - dec) {
                    // ER
                    eventType = EventType.ENTITY_REFERENCE;
                }
            }
            return;
        } finally {
            assertEquals(EventType.ENTITY_REFERENCE, eventType);
        }
    }

    @Test
    public void testLine451() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 2;
        int dec = 1;
        if (ec2 == 3 - dec) {
            // SE*
            eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        } else if (ec2 == 4 - dec) {
            // CH
            eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (ec2 == 5 - dec) {
                // ER
                eventType = EventType.ENTITY_REFERENCE;
            }
        }
        assertEquals(EventType.START_ELEMENT_GENERIC_UNDECLARED, eventType);
    }

    @Test
    public void testLine452() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 2;
        int dec = 2;
        if (ec2 == 3 - dec) {
            // SE*
            eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        } else if (ec2 == 4 - dec) {
            // CH
            eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (ec2 == 5 - dec) {
                // ER
                eventType = EventType.ENTITY_REFERENCE;
            }
        }
        assertEquals(EventType.CHARACTERS_GENERIC_UNDECLARED, eventType);
    }

    @Test
    public void testLine453() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 2;
        int dec = 0;
        if (ec2 == 3 - dec) {
            // SE*
            eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        } else if (ec2 == 4 - dec) {
            // CH
            eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (ec2 == 5 - dec) {
                // ER
                eventType = EventType.ENTITY_REFERENCE;
            }
        }
        assertEquals(EventType.ATTRIBUTE, eventType);
    }

    @Test
    public void testLine454() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 5;
        if (ec2 == 3 - dec) {
            // SE*
            eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        } else if (ec2 == 4 - dec) {
            // CH
            eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (ec2 == 5 - dec) {
                // ER
                eventType = EventType.ENTITY_REFERENCE;
            }
        }
        assertEquals(EventType.ENTITY_REFERENCE, eventType);
    }

    @Test
    public void testLine485() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (ec2 == 1 - dec) {
            // SE*
            eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        } else if (ec2 == 2 - dec) {
            // CH
            eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (ec2 == 3 - dec) {
                // ER
                eventType = EventType.ENTITY_REFERENCE;
            }
        }
        assertEquals(EventType.START_ELEMENT_GENERIC_UNDECLARED, eventType);
    }

    @Test
    public void testLine486() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 2;
        if (ec2 == 1 - dec) {
            // SE*
            eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        } else if (ec2 == 2 - dec) {
            // CH
            eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (ec2 == 3 - dec) {
                // ER
                eventType = EventType.ENTITY_REFERENCE;
            }
        }
        assertEquals(EventType.CHARACTERS_GENERIC_UNDECLARED, eventType);
    }

    @Test
    public void testLine487() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 3;
        if (ec2 == 1 - dec) {
            // SE*
            eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        } else if (ec2 == 2 - dec) {
            // CH
            eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (ec2 == 3 - dec) {
                // ER
                eventType = EventType.ENTITY_REFERENCE;
            }
        }
        assertEquals(EventType.ENTITY_REFERENCE, eventType);
    }

    @Test
    public void testLine488() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 10;
        int dec = 3;
        if (ec2 == 1 - dec) {
            // SE*
            eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        } else if (ec2 == 2 - dec) {
            // CH
            eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (ec2 == 3 - dec) {
                // ER
                eventType = EventType.ENTITY_REFERENCE;
            }
        }
        assertEquals(EventType.ATTRIBUTE, eventType);
    }

    @Test
    public void testLine527() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 4;
        if (ec2 == 4 - dec) {
            // SE*
            eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        } else if (ec2 == 5 - dec) {
            // CH
            eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (ec2 == 6 - dec) {
                // ER
                eventType = EventType.ENTITY_REFERENCE;
            }
        }
        assertEquals(EventType.START_ELEMENT_GENERIC_UNDECLARED, eventType);
    }

    @Test
    public void testLine528() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 5;
        if (ec2 == 4 - dec) {
            // SE*
            eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        } else if (ec2 == 5 - dec) {
            // CH
            eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (ec2 == 6 - dec) {
                // ER
                eventType = EventType.ENTITY_REFERENCE;
            }
        }
        assertEquals(EventType.CHARACTERS_GENERIC_UNDECLARED, eventType);
    }

    @Test
    public void testLine529() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 6;
        if (ec2 == 4 - dec) {
            // SE*
            eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        } else if (ec2 == 5 - dec) {
            // CH
            eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (ec2 == 6 - dec) {
                // ER
                eventType = EventType.ENTITY_REFERENCE;
            }
        }
        assertEquals(EventType.ENTITY_REFERENCE, eventType);
    }

    @Test
    public void testLine530() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 10;
        int dec = 3;
        if (ec2 == 4 - dec) {
            // SE*
            eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        } else if (ec2 == 5 - dec) {
            // CH
            eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (ec2 == 6 - dec) {
                // ER
                eventType = EventType.ENTITY_REFERENCE;
            }
        }
        assertEquals(EventType.ATTRIBUTE, eventType);
    }

    @Test
    public void testLine627() throws Exception {
        EventType eventType = EventType.SELF_CONTAINED;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.SELF_CONTAINED) {
            // SC
            ec2 = 6 - dec;
        } else {
            if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
                // SE*
                ec2 = 7 - dec;
            } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
                // CH
                ec2 = 8 - dec;
            } else {
                if (!isDTD) {
                    dec++;
                }
                if (eventType == EventType.ENTITY_REFERENCE) {
                    // ER
                    ec2 = 9 - dec;
                }
            }
        }
        assertEquals(5, ec2);
    }

    @Test
    public void testLine628() throws Exception {
        EventType eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.SELF_CONTAINED) {
            // SC
            ec2 = 6 - dec;
        } else {
            if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
                // SE*
                ec2 = 7 - dec;
            } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
                // CH
                ec2 = 8 - dec;
            } else {
                if (!isDTD) {
                    dec++;
                }
                if (eventType == EventType.ENTITY_REFERENCE) {
                    // ER
                    ec2 = 9 - dec;
                }
            }
        }
        assertEquals(6, ec2);
    }

    @Test
    public void testLine629() throws Exception {
        EventType eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.SELF_CONTAINED) {
            // SC
            ec2 = 6 - dec;
        } else {
            if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
                // SE*
                ec2 = 7 - dec;
            } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
                // CH
                ec2 = 8 - dec;
            } else {
                if (!isDTD) {
                    dec++;
                }
                if (eventType == EventType.ENTITY_REFERENCE) {
                    // ER
                    ec2 = 9 - dec;
                }
            }
        }
        assertEquals(7, ec2);
    }

    @Test
    public void testLine630() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.SELF_CONTAINED) {
            // SC
            ec2 = 6 - dec;
        } else {
            if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
                // SE*
                ec2 = 7 - dec;
            } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
                // CH
                ec2 = 8 - dec;
            } else {
                if (!isDTD) {
                    dec++;
                }
                if (eventType == EventType.ENTITY_REFERENCE) {
                    // ER
                    ec2 = 9 - dec;
                }
            }
        }
        assertEquals(0, ec2);
    }

    @Test
    public void testLine631() throws Exception {
        EventType eventType = EventType.ENTITY_REFERENCE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.SELF_CONTAINED) {
            // SC
            ec2 = 6 - dec;
        } else {
            if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
                // SE*
                ec2 = 7 - dec;
            } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
                // CH
                ec2 = 8 - dec;
            } else {
                if (!isDTD) {
                    dec++;
                }
                if (eventType == EventType.ENTITY_REFERENCE) {
                    // ER
                    ec2 = 9 - dec;
                }
            }
        }
        assertEquals(8, ec2);
    }

    @Test
    public void testLine632() throws Exception {
        EventType eventType = EventType.ENTITY_REFERENCE;
        boolean isDTD = false;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.SELF_CONTAINED) {
            // SC
            ec2 = 6 - dec;
        } else {
            if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
                // SE*
                ec2 = 7 - dec;
            } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
                // CH
                ec2 = 8 - dec;
            } else {
                if (!isDTD) {
                    dec++;
                }
                if (eventType == EventType.ENTITY_REFERENCE) {
                    // ER
                    ec2 = 9 - dec;
                }
            }
        }
        assertEquals(7, ec2);
    }

    @Test
    public void testLine673() throws Exception {
        EventType eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 3 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 4 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 5 - dec;
            }
        }
        assertEquals(2, ec2);
    }

    @Test
    public void testLine674() throws Exception {
        EventType eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 3 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 4 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 5 - dec;
            }
        }
        assertEquals(3, ec2);
    }

    @Test
    public void testLine675() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 3 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 4 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 5 - dec;
            }
        }
        assertEquals(0, ec2);
    }

    @Test
    public void testLine676() throws Exception {
        EventType eventType = EventType.ENTITY_REFERENCE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 3 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 4 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 5 - dec;
            }
        }
        assertEquals(4, ec2);
    }

    @Test
    public void testLine677() throws Exception {
        EventType eventType = EventType.ENTITY_REFERENCE;
        boolean isDTD = false;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 3 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 4 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 5 - dec;
            }
        }
        assertEquals(3, ec2);
    }

    @Test
    public void testLine708() throws Exception {
        EventType eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 1 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 2 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 3 - dec;
            }
        }
        assertEquals(0, ec2);
    }

    @Test
    public void testLine709() throws Exception {
        EventType eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 1 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 2 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 3 - dec;
            }
        }
        assertEquals(1, ec2);
    }

    @Test
    public void testLine710() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 1 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 2 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 3 - dec;
            }
        }
        assertEquals(0, ec2);
    }

    @Test
    public void testLine711() throws Exception {
        EventType eventType = EventType.ENTITY_REFERENCE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 1 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 2 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 3 - dec;
            }
        }
        assertEquals(2, ec2);
    }

    @Test
    public void testLine712() throws Exception {
        EventType eventType = EventType.ENTITY_REFERENCE;
        boolean isDTD = false;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 1 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 2 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 3 - dec;
            }
        }
        assertEquals(1, ec2);
    }

    @Test
    public void testLine751() throws Exception {
        EventType eventType = EventType.START_ELEMENT_GENERIC_UNDECLARED;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 4 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 5 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 6 - dec;
            }
        }
        assertEquals(3, ec2);
    }

    @Test
    public void testLine752() throws Exception {
        EventType eventType = EventType.CHARACTERS_GENERIC_UNDECLARED;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 4 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 5 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 6 - dec;
            }
        }
        assertEquals(4, ec2);
    }

    @Test
    public void testLine753() throws Exception {
        EventType eventType = EventType.ATTRIBUTE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 4 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 5 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 6 - dec;
            }
        }
        assertEquals(0, ec2);
    }

    @Test
    public void testLine754() throws Exception {
        EventType eventType = EventType.ENTITY_REFERENCE;
        boolean isDTD = true;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 4 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 5 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 6 - dec;
            }
        }
        assertEquals(5, ec2);
    }

    @Test
    public void testLine755() throws Exception {
        EventType eventType = EventType.ENTITY_REFERENCE;
        boolean isDTD = false;
        int ec2 = 0;
        int dec = 1;
        if (eventType == EventType.START_ELEMENT_GENERIC_UNDECLARED) {
            // SE*
            ec2 = 4 - dec;
        } else if (eventType == EventType.CHARACTERS_GENERIC_UNDECLARED) {
            // CH
            ec2 = 5 - dec;
        } else {
            if (!isDTD) {
                dec++;
            }
            if (eventType == EventType.ENTITY_REFERENCE) {
                // ER
                ec2 = 6 - dec;
            }
        }
        assertEquals(4, ec2);
    }
}
