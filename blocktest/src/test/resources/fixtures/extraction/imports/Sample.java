package sample;

import static org.blocktest.BTest.blocktest;
import java.util.List;
import org.blocktest.BTest;
import org.junit.Ignore;
import org.testng.Assert;

public class Sample {
    public void m() {
        int x = 0;
        blocktest("increment").given(x, 1).checkEq(x, 2);
        x++;
    }
}
