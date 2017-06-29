import org.junit.Test;
import static org.junit.Assert.*;

public class TestLinkedListDeque1B {
	// @Test
	// public void testA() {
	// 	StudentLinkedListDeque<Integer> dq = new StudentLinkedListDeque<Integer>();
	// 	LinkedListDequeSolution<Integer> dqs = new LinkedListDequeSolution<Integer>();

	// 	dq.addLast(5);
	// 	dq.addLast(10);
	// 	dq.addLast(15);

	// 	assertEquals(false, dq.isEmpty());
	// }

	// @Test
	// public void testB() {
	// 	StudentLinkedListDeque<Integer> dq = new StudentLinkedListDeque<Integer>();
	// 	LinkedListDequeSolution<Integer> dqs = new LinkedListDequeSolution<Integer>();
	// 	assertEquals(dqs.size(), dq.size());
	// 	assertEquals(dqs.isEmpty(), dq.isEmpty());

	// 	for (int i = 1; i <= 10; i++) {
	// 		int n = StdRandom.uniform(9);
	// 		dq.addFirst(n);
	// 		dqs.addFirst(n);
	// 	}

	// 	while (!dqs.isEmpty()) {
	// 		assertEquals(dqs.get(0), dq.get(0));
	// 		assertEquals(dqs.removeLast(), dq.removeLast());
	// 	}
		
	// }

	// right now looks like the test is failing
	// FailureSequence fs = new FailureSequence();
 //        DequeOperation dequeOp1 = new DequeOperation("addFirst", 5);
 //        DequeOperation dequeOp2 = new DequeOperation("addFirst", 10);
 //        DequeOperation dequeOp3 = new DequeOperation("size");

 //        fs.addOperation(dequeOp1);
 //        fs.addOperation(dequeOp2);
 //        fs.addOperation(dequeOp3);

 //        System.out.println(fs.toString());

	@Test
	public void testC() {
		StudentLinkedListDeque<Integer> dq = new StudentLinkedListDeque<Integer>();
		LinkedListDequeSolution<Integer> dqs = new LinkedListDequeSolution<Integer>();

		FailureSequence fs = new FailureSequence();

		for (int i = 1; i <= 3; i++) {
			//int n = StdRandom.uniform(9);
			dq.addFirst(i);
			dqs.addFirst(i);

			//fs.addOperation("addFirst", i);
		}

		while (!dqs.isEmpty()) {
			assertEquals("size = " + dqs.size() + "\n", dqs.get(0), dq.get(0));
			dqs.removeLast(); dq.removeLast();
		}
		
	}

	@Test
	public void testD() {
		StudentLinkedListDeque<Integer> dq = new StudentLinkedListDeque<Integer>();
		LinkedListDequeSolution<Integer> dqs = new LinkedListDequeSolution<Integer>();

		FailureSequence fs = new FailureSequence();

		for (int i = 1; i <= 3; i++) {
			dq.addFirst(i);
			dqs.addFirst(i);
			fs.addOperation(new DequeOperation("addFirst", i));
		}

		while (dq.size() > 1) {
			dqs.removeLast(); dq.removeLast();
			fs.addOperation( new DequeOperation("removeLast"));
		}

		assertEquals(fs.toString(), dqs.get(0), dq.get(0));
		
	}

	public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestLinkedListDeque1B.class);
	}
}
