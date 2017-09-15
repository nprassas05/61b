public class SpecialString implements Comparable<SpecialString> {
	private String s;
	public SpecialString(String s) {
		this.s = s;
	}
	
	@Override
	public int compareTo(SpecialString sp) {
		int n = s.length();
		int m = sp.s.length();
		
		if (n > m) {
			return 1;
		} else if (m > n) {
			return -1;
		}
		
		return 0;
	}

	@Override
	public String toString() {
		return s;
	}
}