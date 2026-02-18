package am.xo.cdjscrobbler;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import am.xo.cdjscrobbler.SongEvents.NewSongLoadedEvent;
import am.xo.cdjscrobbler.SongEvents.NowPlayingEvent;
import am.xo.cdjscrobbler.SongEvents.ResetEvent;
import am.xo.cdjscrobbler.SongEvents.ScrobbleEvent;
import am.xo.cdjscrobbler.SongEvents.TransitionEvent;
import org.deepsymmetry.beatlink.CdjStatus;
import org.deepsymmetry.beatlink.DeviceUpdate;
import org.deepsymmetry.beatlink.DeviceUpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.Test;
import static org.junit.Assert.*;
import static am.xo.cdjscrobbler.UpdateListener.*;

public class UpdateListenerBlockTest {

    @Test
    public void testLine105() throws Exception {
        LinkedBlockingQueue<SongEvent> songEventQueue = new LinkedBlockingQueue<SongEvent>();
        ResetEvent e = new ResetEvent(new CdjStatus(new java.net.DatagramPacket(new byte[1024], 1024)));
        int deviceNumber = 1;
        Logger logger = LoggerFactory.getLogger(UpdateListener.class);
        try {
            songEventQueue.put(e);
        } catch (InterruptedException ex) {
            /*ex.printStackTrace();*/
        }
        assertEquals("** RESET (Song stopped or changed) **", songEventQueue.iterator().next().toString());
    }
}
