package ru.icc.cells.tabbypdf.detection;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import lombok.Getter;
import lombok.Setter;
import ru.icc.cells.tabbypdf.entities.*;
import ru.icc.cells.tabbypdf.utils.content.PageLayoutAlgorithm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;
import static ru.icc.cells.tabbypdf.detection.TableRegionDetector.*;

public class TableRegionDetectorBlockTest {

    @Test
    public void testLine96() throws Exception {
        ArrayList<TextBlock> textBlocks = new ArrayList<TextBlock>();
        ArrayList<TextBlock> textBlocks2 = new ArrayList<TextBlock>();
        TextBlock tb = new TextBlock();
        tb.add(new TextChunk("foo", 1, 2, 3, 4, FontCharacteristics.newBuilder().setAllCap(true).build()));
        textBlocks.add(tb);
        TextBlock tb2 = new TextBlock();
        tb2.add(new TextChunk("bar", 1, 2, 3, 4, FontCharacteristics.newBuilder().setAllCap(true).build()));
        textBlocks2.add(tb2);
        try {
            ArrayList<TextBlock> rectangles = new ArrayList<>();
            rectangles.addAll(textBlocks);
            {
                assertEquals("[Rectangle(left=1.0, bottom=2.0, right=3.0, top=4.0), Rectangle(left=1.0, bottom=2.0, right=3.0, top=4.0)]", (rectangles).toString());
                return;
            }
        } finally {
        }
    }
}
