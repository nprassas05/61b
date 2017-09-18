import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class HuffmanEncoder {
	public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
		Map<Character, Integer> frequencyTable = new HashMap<>();

		for (int i = 0; i < inputSymbols.length; ++i) {
			char c = inputSymbols[i];

			if (frequencyTable.containsKey(c)) {
				int count = frequencyTable.get(c);
				frequencyTable.put(c, count + 1);
			} else {
				frequencyTable.put(c, 1);
			}
		}

		return frequencyTable;
	}

	public static void main(String[] args) {
		String fileName = args[0];
		char[] charArray = FileUtils.readFile(fileName);
		Map<Character, Integer> frequencyTable = HuffmanEncoder.buildFrequencyTable(charArray);
		BinaryTrie binTrie = new BinaryTrie(frequencyTable);
		ObjectWriter objWriter = new ObjectWriter(fileName + ".huf");
		objWriter.writeObject(binTrie);
		objWriter.writeObject(new Integer(charArray.length));
		Map<Character, BitSequence> lookupTable = binTrie.buildLookupTable();
		List<BitSequence> bitSequenceList = new ArrayList<>();

		for (char c: charArray) {
			bitSequenceList.add(lookupTable.get(c));
		}

		BitSequence aggregateSequence = BitSequence.assemble(bitSequenceList);
		objWriter.writeObject(aggregateSequence);
	}
}