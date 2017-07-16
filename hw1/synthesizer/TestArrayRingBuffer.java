package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<String> arb= new ArrayRingBuffer(10);
        for (int i = 0; i < 10; i++) {
        	arb.enqueue(String.valueOf(i + 1));
        }

        for (String s: arb) {
        	System.out.print(s + " ");
        }
        System.out.println();

        for (int i = 0; i < 5; i++) {
        	System.out.print(arb.dequeue() + " ");
        }
        System.out.println();

        for (String s: arb) {
        	System.out.print(s + " ");
        }
        System.out.println();

    }

    @Test(expected = RuntimeException.class)
    public void testB() {
    	ArrayRingBuffer<String> arb= new ArrayRingBuffer(10);
    	for (int i = 0; i < 3; ++i) {
    		arb.enqueue(String.valueOf((char) ('A' + i)));
    	}

    	for (int i = 0; i < 3; ++i) {
    		System.out.print(arb.dequeue() + " ");
    	}
    	System.out.println();

    	/* here an exception should be thrown for trying to dequeue
    	   from an empty buffer */
    	arb.dequeue();
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
