public class OffByN implements CharacterComparator {
	int n;

	/* constructor */
	public OffByN(int n) {
		this.n = n;
	}

	@Override
	public boolean equalChars(char c1, char c2) {
		return Math.abs(c1 - c2) == n;
	}
}