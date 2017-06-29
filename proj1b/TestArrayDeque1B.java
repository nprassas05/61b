import org.junit.Test;
import static org.junit.Assert.*;

public class TestArrayDeque1B {
	/* perform all tests with dequeus that hold integers,
	   as specified in the spec */
	@Test
	public void testAddLast() {
		StudentArrayDeque<Integer> dq1 = new StudentArrayDeque<Integer>();
		
		for (int i = 1; i <= 9; ++i) {
			dq1.addLast(i * 10);
		}

		Integer expectedLast = new Integer(90);
		Integer resultLast = dq1.removeLast();

		assertEquals(expectedLast, resultLast);

	}	
	
	/* failing test here */
	@Test
	public void testB() {
		StudentArrayDeque<Integer> dq = new StudentArrayDeque<Integer>();
		ArrayDequeSolution<Integer> dqs = new ArrayDequeSolution<Integer>();

		FailureSequence fs = new FailureSequence();
				
		for (int i = 1; i <= 8; ++i) {
			dqs.addFirst(i * 10);
			dq.addFirst(i * 10);

			fs.addOperation(new DequeOperation("addFirst", i));
		}

		for (int i = 0; i < 5; ++i) {
			dqs.removeFirst();
			dq.removeFirst();

			fs.addOperation(new DequeOperation("removeFirst"));
		}

		assertEquals(fs.toString(), dqs.get(2), dq.get(2)); // here we get a failure

	}

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestArrayDeque1B.class);
	}
}
	
