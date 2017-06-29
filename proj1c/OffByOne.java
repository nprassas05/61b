public class OffByOne implements CharacterComparator {
	@Override
	public boolean equalChars(char c1, char c2) {
		int difference = Math.abs(c1 - c2);
		return difference == 1;
	}

}