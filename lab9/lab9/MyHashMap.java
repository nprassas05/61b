package lab9;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class MyHashMap<K, V> implements Map61B<K, V> {
	private class EntryNode {
		private K key;
		private V value;

		public EntryNode(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public void setValue(V value) {
			this.value = value;
		}
	}

	private static final int RESIZE_FACTOR = 2;

	int numBuckets;
	int size;
	double loadFactor;
	LinkedList<EntryNode>[] buckets;

	public MyHashMap() {
		this(100, 1.25);
	}

	public MyHashMap(int initialize) {
		this(initialize, 1.25);
	}

	public void resize(int cap) {
		MyHashMap temp = new MyHashMap(cap);

		for (int i = 0; i < numBuckets; ++i) {
			LinkedList<EntryNode> list = buckets[i];

			for (EntryNode e: list) {
				temp.put(e.key, e.value);
			}
		}

		this.buckets = temp.buckets;
		numBuckets = cap;
	}

	public MyHashMap(int initialize, double loadFactor) {
		buckets = (LinkedList<EntryNode>[]) new LinkedList[initialize];
		numBuckets = initialize;
		this.loadFactor = loadFactor;

		for (int i = 0; i < numBuckets; ++i)
			buckets[i] = new LinkedList<EntryNode>();
	}

	public void put(K key, V value) {
		int index = hash(key);
		LinkedList<EntryNode> list = buckets[index];
		EntryNode entry = getEntry(key);

		if ((double) size / numBuckets > loadFactor) {
			resize(numBuckets * RESIZE_FACTOR);
		}

		if (entry == null) {
			buckets[index].addFirst(new EntryNode(key, value));
			++size;
		} else {
			entry.value = value;
		}		
	}

	public boolean containsKey(K key) {
		int index = key.hashCode();

		return buckets[index].contains(key);
	}

	public void clear() {
		size = 0;
		buckets = (LinkedList<EntryNode>[]) new LinkedList[100];

		for (int i = 0; i < numBuckets; ++i)
			buckets[i] = new LinkedList<EntryNode>();
	}

	@Override
    public Iterator<K> iterator() {
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    public int hash(K key) {
    	return (key.hashCode() & 0x7fffffff) % numBuckets;
    }

	public V get(K key) {
		int index = hash(key);
		LinkedList<EntryNode> list = buckets[index];

		EntryNode entry = getEntry(key);

		return entry != null ? entry.value : null;
	}

	public EntryNode getEntry(K key) {
		int index = hash(key);
		LinkedList<EntryNode> list = buckets[index];

		for (EntryNode entry: list) {
			if (entry.key == (key)) {
				return entry;
			}
		}

		return null;
	}

	@Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();

        for (int i = 0; i < numBuckets; ++i) {
        	LinkedList<EntryNode> list = buckets[i];

        	for (EntryNode e: list) {
        		keySet.add(e.key);
        	}
        }

        return keySet;
    }
}