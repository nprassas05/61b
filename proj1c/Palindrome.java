public class Palindrome {
	public static Deque<Character> wordToQueue(String word) {
		Deque<Character> dq = new LinkedListDequeSolution<Character>();
		for (int i = 0; i < word.length(); ++i) {
			char c = word.charAt(i);
			dq.addLast(c);
		}
		return dq;
	}

	public static boolean isPalindrome(String word) {
		Deque<Character> dq = wordToQueue(word);
		return palindromeHelper(dq);
	}

	private static boolean palindromeHelper(Deque<Character> dq) {
		if (dq.size() == 1 || dq.size() == 0) {
			return true;
		}

		char front = dq.removeFirst();
		char back = dq.removeLast();
		if (front != back) {	
			return false;
		}
		
		return palindromeHelper(dq);
	}

	public static boolean isPalindrome(String word, CharacterComparator cc) {
		int front = 0;
		int back = word.length() - 1;

		while (front < back) {
			char cFront = word.charAt(front);
			char cBack = word.charAt(back);

			if (!cc.equalChars(cFront, cBack)) {
				return false;
			}
			++front;
			--back;
		}

		return true;
	}

}
