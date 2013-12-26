import java.util.BitSet;
import java.util.Random;

public class HopscotchHashTable<AnyType> {

	public HopscotchHashTable() {
		this(TABLE_SIZE);
	}

	public HopscotchHashTable(int size) {
		allocateArray(size);
		doClear();
	}

	public void makeEmpty() {
		doClear();
	}

	private void doClear() {
		theSize = 0;
		for (int i = 0; i < array.length; i++)
			array[i] = null;
	}

	private int myhash(AnyType x) {
		int hashVal = x.hashCode();
		hashVal %= array.length;
		if (hashVal < 0) {
			hashVal += array.length;
		}
		return hashVal;
	}

	private boolean insert(AnyType key) {
		int actualSlot = myhash(key);
		
		if (contains(key) || theSize == array.length) {
			System.out.println("Table is full OR Key is already present... Can not insert ");
			return false;
		}

		for (int i = actualSlot; i < actualSlot + MAX_HOP_SIZE; i++) {
			if (array[i % TABLE_SIZE] == null) { // insert at position i
				array[i % TABLE_SIZE] = new HashEntry<AnyType>(key);
				array[actualSlot].hop.set(i - actualSlot);
				theSize++;
				return true;
			} else if (array[i % TABLE_SIZE].element == null) {
				array[i % TABLE_SIZE].element = key;
				array[actualSlot].hop.set(i - actualSlot);
				theSize++;
				return true;
			}
		}

		System.out.println("All near slots are full ");
		// Couldn't find position less than Hope size :(
		int nextOpenSlot = getNextOpenSlot(actualSlot);
		if (nextOpenSlot == -1) {
			return false;
		}

		while (nextOpenSlot >= actualSlot + MAX_HOP_SIZE) {
			nextOpenSlot = moveDown(nextOpenSlot);
		}
		
		return insert(key);
	}

	/**
	 * @param nextOpenSlot
	 */
	private int moveDown(int nextOpenSlot) {

		System.out.println("Moving down");

		for (int i = nextOpenSlot - MAX_HOP_SIZE + 1; i < nextOpenSlot; i++) {

			if (array[nextOpenSlot % TABLE_SIZE] == null) {
				array[nextOpenSlot % TABLE_SIZE] = new HashEntry<AnyType>(null);
			}

			if (possibleToMove(array[i % TABLE_SIZE], nextOpenSlot)) {
				int oldDiatance = getDistanceFromActualSlot(array[i
						% TABLE_SIZE].element, i % TABLE_SIZE);
				array[nextOpenSlot % TABLE_SIZE].element = array[i % TABLE_SIZE].element;
				int originalPosition = myhash(array[i % TABLE_SIZE].element);
				int newDistance = nextOpenSlot - originalPosition;
				array[originalPosition].hop.clear(oldDiatance);
				array[originalPosition].hop.set(newDistance);
				array[i % TABLE_SIZE].element = null;
				return i;
			}
		}
		return nextOpenSlot;
	}

	private int getNextOpenSlot(int actualSlot) {
		for (int i = (actualSlot + MAX_HOP_SIZE); i != actualSlot; i++) {
			if (array[i % TABLE_SIZE] == null
					|| array[i % TABLE_SIZE].element == null) {
				System.out.println(" Next Open Slot at " + (i % TABLE_SIZE));
				return i;
			}
		}
		return -1;
	}

	private int getDistanceFromActualSlot(AnyType element, int i) {
		int hashValue = myhash(element);
		int distance = 0;
		while (hashValue != i) {
			hashValue = ++hashValue % TABLE_SIZE;
			distance++;
		}
		return distance;
	}

	private boolean possibleToMove(HashEntry<AnyType> hashEntry,
			int nextOpenSlot) {
		BitSet hop = hashEntry.hop;
		if (hop.nextSetBit(0) == -1)
			return false;
		return true;
	}

	public boolean contains(AnyType key) {
		int hashValue = myhash(key);
		if (array[hashValue] == null) {
			return false;
		}
		BitSet hopOfEntry = array[hashValue].hop;
		for (int i = 0; i < MAX_HOP_SIZE; i++) {
			if (hopOfEntry.get(i)
					&& array[(hashValue + i) % TABLE_SIZE].element.equals(key)) {
				return true;
			}
		}
		return false;
	}

	public boolean remove(AnyType key) {
		int hashValue = myhash(key);
		if (contains(key)) {
			BitSet hopOfEntry = array[hashValue].hop;
			for (int i = 0; i < MAX_HOP_SIZE; i++) {
				if (hopOfEntry.get(i) && array[hashValue + i].equals(key)) {
					array[hashValue + i].element = null;
					hopOfEntry.clear(i);
					theSize--;
					return true;
				}
			}
		}
		return false;
	}

	private static final int TABLE_SIZE = 20;
	private static final int MAX_HOP_SIZE = 8;

	private HashEntry<AnyType>[] array; // The array of elements
	private int theSize; // Current size

	@SuppressWarnings("unchecked")
	private void allocateArray(int arraySize) {
		array = new HashEntry[TABLE_SIZE];
	}

	private static class HashEntry<AnyType> {
		public AnyType element; // the element
		public BitSet hop; // false if marked deleted

		public HashEntry(AnyType e) {
			this(e, new BitSet(MAX_HOP_SIZE));
		}

		public HashEntry(AnyType e, BitSet hop) {
			this.element = e;
			this.hop = hop;
		}

		@Override
		public String toString() {
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append(element + "|");
			for (int i = 0; i < MAX_HOP_SIZE; i++) {
				strBuffer.append(hop.get(i) ? "1" : "0");
			}
			return strBuffer.toString();
		}
	}

	@Override
	public String toString() {

		for (int i = 0; i < TABLE_SIZE; i++) {
			System.out.println(i + "| " + array[i]);
		}
		return "----------------------------------------------";
	}

	public static void main(String[] args) {
		HopscotchHashTable<Integer> hashTable = new HopscotchHashTable<Integer>();
		Random randomGenerator = new Random();
		for (int i = 0; i < 30; i++) {
			Integer value  = new Integer(randomGenerator.nextInt(50));
			System.out.println("\n\ncontains(" + value +") = "+  hashTable.contains(value));
			System.out.println("Inserting " + value + " in hashtable, hash(" + value + ") = "+ hashTable.myhash(value));
			hashTable.insert(value);
			System.out.println("Value inserted");
			System.out.println(hashTable);
			System.out.println("contains(" + value +") = "+  hashTable.contains(value));
		}
	}
}
