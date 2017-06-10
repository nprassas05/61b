/**
 * Created by ELENA on 6/2/17.
 */
import static org.junit.Assert.*;
import org.junit.Test;

public class FlikTest {
    @Test
    public void testA() {
        int s = 5;
        int t = 5;

        assertTrue(Flik.isSameNumber(s, t));
    }

    @Test
    public void testB() {
        int s = 128;
        int t = 128;

        assertTrue(Flik.isSameNumber(s, t));
    }

    @Test
    public void testC() {
        int s = 127;
        int t = 127;

        assertTrue(Flik.isSameNumber(s, t));
    }

    @Test
    public void testD() {
        int s = 128;
        int t = 128;

        Integer S = s;
        Integer T = t;

        String m = "\ns: " + new Integer(s) + "\nt: " + new Integer(t);

        assertTrue(m, Flik.isSameNumber(s, t));
    }

    @Test
    public void testE() {
        int s = 130;
        int t = 130;

        assertTrue(Flik.isSameNumber(s, t));
    }
}
