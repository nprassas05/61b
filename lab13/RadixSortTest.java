public class RadixSortTest {
	public static void main(String[] args) {
		String[] asciis = {"John", "Carter", "Abel", "Kyle", "Abe", "Peanut", "Aaron", "Simpson", "Jeff", "Joe"};
		String[] sorted = RadixSort.sort(asciis);

		for (String s: sorted) {
			System.out.print(s + " ");
		} System.out.println();
	}
}