public class MyTest {
	public static void main(String[] args) {
		int[] someNegative = {9, 5, -4, 2, 1, -2, 5, 3, 0, -2, 3, 1, 1};
		int[] sorted = CountingSort.betterCountingSort(someNegative);

		for (int p: sorted) {
			System.out.print(p + " ");
		} System.out.println();
	}
}