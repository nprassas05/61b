public class HuffmanDecoder {
	public static void main(String[] args) {
		String inFile = args[0];
		String outFile = args[1];

		ObjectReader objReader = new ObjectReader(inFile);
		//ObjectWriter objwriter = new ObjectWriter(outFile);

		BinaryTrie binTrie = (BinaryTrie) objReader.readObject();
		int numSymbols = (int) objReader.readObject();

		BitSequence bitSequence = (BitSequence) objReader.readObject();

		char[] charSequence = new char[numSymbols];

		int i = 0;
		while (bitSequence.length() > 0) {
			Match m = binTrie.longestPrefixMatch(bitSequence);
			charSequence[i++] = m.getSymbol();
			bitSequence = bitSequence.allButFirstNBits(m.getSequence().length());
		}

		FileUtils.writeCharArray(outFile, charSequence);
	}
}