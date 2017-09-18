import java.util.Map;
import java.util.HashMap;
import edu.princeton.cs.algs4.MinPQ;
import java.io.Serializable;

public class BinaryTrie implements Serializable {
	// alphabet size
	public static final int R = 256;

	// Huffman trie node
    private static class TrieNode implements Comparable<TrieNode>, Serializable {
        private final char ch;
        private final int freq;
        private final TrieNode left, right;

        TrieNode(char ch, int freq, TrieNode left, TrieNode right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        // compare, based on frequency
        public int compareTo(TrieNode that) {
            return this.freq - that.freq;
        }
    }

    /* root of the trie */
    TrieNode root;

    /* given a frequency table of each char in the bit stream,
	   create in the Huffman decoding trie 
	   General procedure:
	   		- remove two lowest frequencys and join them together into
	   		  a tree
	   		- repeat until we get to the very last one
	*/
	public BinaryTrie(Map<Character, Integer> frequencyTable) {
		MinPQ<TrieNode> pq = new MinPQ<>();
		for (Map.Entry<Character, Integer> entry: frequencyTable.entrySet()) {
			if (entry.getValue() > 0) {
				pq.insert(new TrieNode(entry.getKey(), entry.getValue(), null, null));
			}
		}

		while (pq.size() > 1) {
			TrieNode x = pq.delMin();
			TrieNode y = pq.delMin();

			TrieNode parent = new TrieNode('\0', x.freq + y.freq, x, y);
			pq.insert(parent);
		}

		root = pq.delMin();
	}

	public Match longestPrefixMatch(BitSequence querySequence) {
		return longestPrefixMatch(querySequence, root, -1);
	}

	private Match longestPrefixMatch(BitSequence querySequence, TrieNode node, int i) {
		if (node.isLeaf()) {
			return new Match(querySequence.firstNBits(i + 1), node.ch);
		} else if (querySequence.bitAt(i + 1) == 0) {
			return longestPrefixMatch(querySequence, node.left, i + 1);
		} else {
			return longestPrefixMatch(querySequence, node.right, i + 1);
		}
	}

	public Map<Character, BitSequence> buildLookupTable() {
		Map<Character, BitSequence> lookupTable = new HashMap<>();
		buildLookupTable(root, new BitSequence(""), lookupTable);
		return lookupTable;
	}

	private void buildLookupTable(TrieNode node, BitSequence b, Map<Character, BitSequence> table) {
		if (node.isLeaf()) {
			table.put(node.ch, b);
		} else {
			buildLookupTable(node.left, b.appended(0), table);
			buildLookupTable(node.right, b.appended(1), table);
		}
	}

	public void printTrie(TrieNode n) {
		if (n.isLeaf()) {
			System.out.println("leaf right here thoooo, " + n.ch + ": " + n.freq);
		} else {
			printTrie(n.left);
			System.out.println(n.ch + ": " + n.freq);
			printTrie(n.right);
		}
	}

	public void printTrie() {
		printTrie(root);
	}
}