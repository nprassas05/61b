package creatures;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

/** Tests the clorus class   
 *  
 */

public class TestClorus {

    /* Replace with the magic word given in lab.
     * If you are submitting early, just put in "early" */
    public static final String MAGIC_WORD = "";

    @Test
    public void testBasics() {
        Clorus c = new Clorus(2);
        assertEquals(2, c.energy(), 0.01);

        assertEquals(new Color(34, 0, 231), c.color());
    }

    @Test
    public void testReplicate() {
        Clorus p = new Clorus(2.00);

        Clorus p2 = p.replicate();

        assertEquals(p.energy(), 1.00, 0.001);
        assertEquals(p2.energy(), 1.00, 0.001);

        assertNotSame("These guys should be but are NOT different objects in memory", p, p2);

    }

    @Test
    public void testChoose() {
        Clorus p = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Plip());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        /* test that we stay if there are no empty spaces */
        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        /* test that if there is at least one empty space and that there exists
           one or more nearby plips, that we attack a plip randomly */
        surrounded.put(Direction.TOP, new Empty());
        actual = p.chooseAction(surrounded);
        expected = new Action(Action.ActionType.ATTACK, Direction.BOTTOM);
        assertEquals(expected, actual);

        /* test if our energy is greater than or equal to one, that we replicate
           to a random nearby square, given there are no plips and some empty spaces */
        surrounded.put(Direction.BOTTOM, new Empty());
        surrounded.put(Direction.LEFT, new Empty());
        actual = p.chooseAction(surrounded);
        expected = new Action(Action.ActionType.REPLICATE, Direction.BOTTOM);
        Action expected2 = new Action(Action.ActionType.REPLICATE, Direction.LEFT);
        assertTrue(actual.equals(expected) || actual.equals(expected2));

        /* create new Clorus with energy less than 1.0, and test if that clorus chooses
           to move to a random empty square */
        p = new Clorus(0.8);
        expected = new Action(Action.ActionType.MOVE, Direction.BOTTOM);
        expected2 = new Action(Action.ActionType.MOVE, Direction.LEFT);
        actual = p.chooseAction(surrounded);
        assertTrue(actual.equals(expected) || actual.equals(expected2));
        


    }

    public static void main(String[] args) {
        System.exit(jh61b.junit.textui.runClasses(TestClorus.class));
    }
} 
