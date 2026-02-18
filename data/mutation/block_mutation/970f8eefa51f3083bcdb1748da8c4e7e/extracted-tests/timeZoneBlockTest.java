package com.maxmind.geoip;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.maxmind.geoip.timeZone.*;

public class timeZoneBlockTest {

    @Test
    public void testLine40() throws Exception {
        String region = "01";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Buenos_Aires", timezone);
    }

    @Test
    public void testLine41() throws Exception {
        String region = "02";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Catamarca", timezone);
    }

    @Test
    public void testLine42() throws Exception {
        String region = "03";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Tucuman", timezone);
    }

    @Test
    public void testLine43() throws Exception {
        String region = "04";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Rio_Gallegos", timezone);
    }

    @Test
    public void testLine44() throws Exception {
        String region = "05";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Cordoba", timezone);
    }

    @Test
    public void testLine45() throws Exception {
        String region = "06";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Tucuman", timezone);
    }

    @Test
    public void testLine46() throws Exception {
        String region = "07";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Buenos_Aires", timezone);
    }

    @Test
    public void testLine47() throws Exception {
        String region = "08";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Buenos_Aires", timezone);
    }

    @Test
    public void testLine48() throws Exception {
        String region = "09";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Tucuman", timezone);
    }

    @Test
    public void testLine49() throws Exception {
        String region = "10";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Jujuy", timezone);
    }

    @Test
    public void testLine50() throws Exception {
        String region = "11";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/San_Luis", timezone);
    }

    @Test
    public void testLine51() throws Exception {
        String region = "12";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/La_Rioja", timezone);
    }

    @Test
    public void testLine52() throws Exception {
        String region = "13";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Mendoza", timezone);
    }

    @Test
    public void testLine53() throws Exception {
        String region = "14";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Buenos_Aires", timezone);
    }

    @Test
    public void testLine54() throws Exception {
        String region = "15";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/San_Luis", timezone);
    }

    @Test
    public void testLine55() throws Exception {
        String region = "16";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Buenos_Aires", timezone);
    }

    @Test
    public void testLine56() throws Exception {
        String region = "17";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Salta", timezone);
    }

    @Test
    public void testLine57() throws Exception {
        String region = "18";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/San_Juan", timezone);
    }

    @Test
    public void testLine58() throws Exception {
        String region = "19";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/San_Luis", timezone);
    }

    @Test
    public void testLine59() throws Exception {
        String region = "20";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Rio_Gallegos", timezone);
    }

    @Test
    public void testLine60() throws Exception {
        String region = "21";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Buenos_Aires", timezone);
    }

    @Test
    public void testLine61() throws Exception {
        String region = "22";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Catamarca", timezone);
    }

    @Test
    public void testLine62() throws Exception {
        String region = "23";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Ushuaia", timezone);
    }

    @Test
    public void testLine63() throws Exception {
        String region = "24";
        String timezone = null;
        if ("01".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("02".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("03".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("04".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("05".equals(region)) {
            timezone = "America/Argentina/Cordoba";
        } else if ("06".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("07".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("08".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("09".equals(region)) {
            timezone = "America/Argentina/Tucuman";
        } else if ("10".equals(region)) {
            timezone = "America/Argentina/Jujuy";
        } else if ("11".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("12".equals(region)) {
            timezone = "America/Argentina/La_Rioja";
        } else if ("13".equals(region)) {
            timezone = "America/Argentina/Mendoza";
        } else if ("14".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("15".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("16".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("17".equals(region)) {
            timezone = "America/Argentina/Salta";
        } else if ("18".equals(region)) {
            timezone = "America/Argentina/San_Juan";
        } else if ("19".equals(region)) {
            timezone = "America/Argentina/San_Luis";
        } else if ("20".equals(region)) {
            timezone = "America/Argentina/Rio_Gallegos";
        } else if ("21".equals(region)) {
            timezone = "America/Argentina/Buenos_Aires";
        } else if ("22".equals(region)) {
            timezone = "America/Argentina/Catamarca";
        } else if ("23".equals(region)) {
            timezone = "America/Argentina/Ushuaia";
        } else if ("24".equals(region)) {
        }
        assertEquals("America/Argentina/Tucuman", timezone);
    }
}
