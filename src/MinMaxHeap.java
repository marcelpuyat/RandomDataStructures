import java.util.List;
import java.util.ArrayList;


public class MinMaxHeap<T extends Comparable<T>> {
	
	// Used to represent levels in the heap. Min means it must be less than both of its children,
	// and max means it must be greater than both of its children. Even levels are max, odd levels are min.
	private enum Level {MIN, MAX;}
	
	// Stores the elements of the heap. This is used instead of nodes and pointers to save space. Any node's
	// children are stored at index * 2 and index * 2 + 1, and any child's parent is stored at floor(log_2(index)).
	private List<T> internalArray;
	
	/**
	 * Initialize empty heap
	 */
	public MinMaxHeap() {
		internalArray = new ArrayList<T>();
	}
	
	/**
	 * Initialize heap with all elems in list. Takes O(nlog(n)) time.
	 * @param elems
	 */
	public MinMaxHeap(List<T> elems) {
		internalArray = new ArrayList<T>(elems.size());
		for (T elem : elems) {
			insert(elem);
		}
	}
	
	/**
	 * Initialize heap with all elems in array. Takes O(nlog(n)) time.
	 * @param elems
	 */
	public MinMaxHeap(T[] elems) {
		internalArray = new ArrayList<T>(elems.length);
		for (T elem : elems) {
			insert(elem);
		}
	}
	
	/**
	 * Inserts element into heap. Takes log(n) time.
	 * @param elem
	 */
	public void insert(T elem) {
		int indexOfInsertion = internalArray.size();
		internalArray.add(elem);
		bubbleUp(indexOfInsertion);
	}
	
	/**
	 * Returns minimum element (without removing it). Takes O(1) time.
	 */
	public T findMin() {
		if (internalArray.size() == 0) {
			return null;
		}
		else if (internalArray.size() == 1) {
			return internalArray.get(0);
		}
		else {
			
			// When there are more than 2 elements, must check min between left and right child of root element
			// becaues the zeroth level is a MAX level and the first level is a MIN level.
			T leftChildOfRoot = internalArray.get(1);
			if (internalArray.size() == 2) {
				return leftChildOfRoot;
			}
			T rightChildOfRoot = internalArray.get(2);
			if (leftChildOfRoot.compareTo(rightChildOfRoot) < 0) {
				return leftChildOfRoot;
			} else {
				return rightChildOfRoot;
			}
		}
	}
	
	/**
	 * Removes minimum element from heap. Takes log(n) time.
	 * @return
	 */
	public T removeMin() {
		if (internalArray.size() == 0) {
			return null;
		}
		else if (internalArray.size() == 1) {
			
			T minValue = internalArray.get(0);
			internalArray.remove(0);
			return minValue;
		}
		else {
			T leftChildOfRoot = internalArray.get(1);
			if (internalArray.size() == 2) {
				internalArray.remove(1);
				return leftChildOfRoot;
			}
			T rightChildOfRoot = internalArray.get(2);
			if (leftChildOfRoot.compareTo(rightChildOfRoot) < 0) {
				replaceMinWithNewMin(1);
				return leftChildOfRoot;
			} else {
				replaceMinWithNewMin(2);
				return rightChildOfRoot;
			}
		}
	}
	
	/**
	 * Returns max element (without removing it). Takes O(1) time. 
	 * @return
	 */
	public T findMax() {
		if (internalArray.size() == 0) {
			return null;
		} else {
			return internalArray.get(0);
		}
	}
	
	/**
	 * Removes max element. Takes log(n) time.
	 * @return
	 */
	public T removeMax() {
		if (internalArray.size() == 0) {
			return null;
		} else {
			T maxValue = internalArray.get(0);
			replaceMaxWithNewMax();
			return maxValue;
		}
	}
	
	/**
	 * Removes max element and replaces it with next max element in heap.
	 */
	private void replaceMaxWithNewMax() {
		// Places last leaf into position of deleted max.
		T lastLeaf = internalArray.get(internalArray.size() - 1);
		if (internalArray.size() > 0) {
			internalArray.set(0, lastLeaf);
			internalArray.remove(internalArray.size() - 1);
			
			// This will shift the newly inserted leaf to ensure the max value will be in its position
			trickleDownMax();
		}
	}
	
	/**
	 * Removes min element and replaces it with next min element in heap.
	 * @param indexOfMin
	 */
	private void replaceMinWithNewMin(int indexOfMin) {
		// Places last leaf into position of deleted min.
		T lastLeaf = internalArray.get(internalArray.size() - 1);
		if (internalArray.size() > 0) {
			internalArray.set(indexOfMin, lastLeaf);
			internalArray.remove(internalArray.size() - 1);
			
			if (internalArray.size() > 2) {
				// This will shift the newly inserted leaf to ensure the min value will be in its position
				trickleDownMin(indexOfMin);
			}
		}
	}
	
	private void trickleDownMin(int currIndex) {
		T currValue = internalArray.get(currIndex);
		while (hasChildren(currIndex)) {
			int leftChildIndex = getIndexOfLeftChild(currIndex);
			int rightChildIndex = leftChildIndex + 1;
			if (rightChildIndex > internalArray.size() - 1) rightChildIndex = -1;
			int firstGrandchildIndex = getIndexOfLeftChild(leftChildIndex);
			int thirdGrandchildIndex = getIndexOfLeftChild(rightChildIndex);
			
			if (firstGrandchildIndex == -1) {
				// Case where no grandchildren, so just have to compare to children.
				
				swapWithMinChildIfNeeded(currIndex, currValue, leftChildIndex,
						rightChildIndex);
				break;
			} else {
				// Case where we have grandchildren. Swap with largest one if we we are less than it.
				int newIndexAfterSwap = swapWithMinGrandchild(currIndex, currValue,
								firstGrandchildIndex, thirdGrandchildIndex);
				if (currIndex == newIndexAfterSwap) break; // End loop once we didnt have to swap with any grandchild
				else currIndex = newIndexAfterSwap;
			}
			
			
		}
	}

	private void swapWithMinChildIfNeeded(int currIndex, T currValue,
			int leftChildIndex, int rightChildIndex) {
		// If less than either of the children, swap with the smaller one.
		if (rightChildIndex != -1) {
			
			// Case where both children exist. Check min between both, and compare to curr index
			T leftChildValue = internalArray.get(leftChildIndex);
			T rightChildValue = internalArray.get(rightChildIndex);
			T minChild = leftChildValue.compareTo(rightChildValue) < 0 ? leftChildValue : rightChildValue;
			
			if (currValue.compareTo(minChild) > 0) {
				
				// If we are greater than the min child, swap and then we're done.
				if (minChild == leftChildValue) {
					swapValuesAtIndices(currIndex, leftChildIndex);
				} 
				
				else /* maxChild == rightChildValue */ {
					swapValuesAtIndices(currIndex, rightChildIndex);
				}
			}
		} else if (leftChildIndex != -1) {
			
			// Case where only left child exists. Check if it is less than curr index.
			T leftChildValue = internalArray.get(leftChildIndex);
			if (currValue.compareTo(leftChildValue) > 0) {
				swapValuesAtIndices(leftChildIndex, currIndex);
			}
		}
	}
	
	private void trickleDownMax() {
		int currIndex = 0;
		T currValue = internalArray.get(currIndex);
		while (hasChildren(currIndex)) {
			int leftChildIndex = getIndexOfLeftChild(currIndex);
			int rightChildIndex = leftChildIndex + 1;
			if (rightChildIndex > internalArray.size() - 1) rightChildIndex = -1;
			int firstGrandchildIndex = getIndexOfLeftChild(leftChildIndex);
			int thirdGrandchildIndex = getIndexOfLeftChild(rightChildIndex);
			
			if (firstGrandchildIndex == -1) {
				// Case where no grandchildren, so just have to compare to children.
				
				swapWithMaxChildIfNeeded(currIndex, currValue, leftChildIndex,
						rightChildIndex);
				break;
			} else {
				// Case where we have grandchildren. Swap with largest one if we we are less than it.
				int newIndexAfterSwap = swapWithMaxGrandchild(currIndex, currValue,
								firstGrandchildIndex, thirdGrandchildIndex);
				if (currIndex == newIndexAfterSwap) break; // End loop once we didnt have to swap with any grandchild
				else currIndex = newIndexAfterSwap;
			}
			
			
		}
	}

	private void swapWithMaxChildIfNeeded(int currIndex, T currValue,
			int leftChildIndex, int rightChildIndex) {
		// If less than either of the children, swap with the smaller one.
		if (rightChildIndex != -1) {
			
			// Case where both children exist. Check max between both, and compare to curr index
			T leftChildValue = internalArray.get(leftChildIndex);
			T rightChildValue = internalArray.get(rightChildIndex);
			T maxChild = leftChildValue.compareTo(rightChildValue) > 0 ? leftChildValue : rightChildValue;
			
			if (currValue.compareTo(maxChild) < 0) {
				
				// If we are less than the max child, swap and then we're done.
				if (maxChild == leftChildValue) {
					swapValuesAtIndices(currIndex, leftChildIndex);
				} 
				
				else /* maxChild == rightChildValue */ {
					swapValuesAtIndices(currIndex, rightChildIndex);
				}
			}
		} else if (leftChildIndex != -1) {
			
			// Case where only left child exists. Check if it is greater than curr index.
			T leftChildValue = internalArray.get(leftChildIndex);
			if (currValue.compareTo(leftChildValue) < 0) {
				swapValuesAtIndices(leftChildIndex, currIndex);
			}
		}
	}
	
	/**
	 * Swaps elem at curr index with its minimum grandchild if it is less than curr value. If so
	 * returns index of this grandchild, and if not, returns currIndex.
	 * 
	 * @param currIndex
	 * @param currValue
	 * @param firstGrandchildIndex
	 * @param thirdGrandchildIndex
	 * @return
	 */
	private int swapWithMinGrandchild(int currIndex, T currValue,
			int firstGrandchildIndex, int thirdGrandchildIndex) {
		int secondGrandchildIndex = firstGrandchildIndex + 1;
		int fourthGrandchildIndex = thirdGrandchildIndex == -1 ? -1 : thirdGrandchildIndex + 1;
		
		T firstGrandchildValue = getValue(firstGrandchildIndex);
		T secondGrandchildValue = getValue(secondGrandchildIndex);
		T thirdGrandchildValue = getValue(thirdGrandchildIndex);
		T fourthGrandchildValue = getValue(fourthGrandchildIndex);
		
		T minGrandchild = getMinGrandchild(firstGrandchildValue,
				secondGrandchildValue, thirdGrandchildValue,
				fourthGrandchildValue);
		
		if (minGrandchild.compareTo(currValue) < 0) {
			int minGrandchildIndex = 0;
			if (minGrandchild == firstGrandchildValue) {
				minGrandchildIndex = firstGrandchildIndex;
			} else if (minGrandchild == secondGrandchildValue) {
				minGrandchildIndex = secondGrandchildIndex;
			} else if (minGrandchild == thirdGrandchildValue) {
				minGrandchildIndex = thirdGrandchildIndex;
			} else {
				minGrandchildIndex = fourthGrandchildIndex;
			}
			swapValuesAtIndices(minGrandchildIndex, currIndex);
			currIndex = minGrandchildIndex;
		}
		return currIndex;
	}

	/**
	 * Swaps elem at curr index with its max grandchild if it is greater than curr value. If so
	 * returns index of this grandchild, and if not, returns currIndex.
	 * 
	 * @param currIndex
	 * @param currValue
	 * @param firstGrandchildIndex
	 * @param thirdGrandchildIndex
	 * @return
	 */
	private int swapWithMaxGrandchild(int currIndex, T currValue,
			int firstGrandchildIndex, int thirdGrandchildIndex) {
		int secondGrandchildIndex = firstGrandchildIndex + 1;
		int fourthGrandchildIndex = thirdGrandchildIndex == -1 ? -1 : thirdGrandchildIndex + 1;
		
		T firstGrandchildValue = getValue(firstGrandchildIndex);
		T secondGrandchildValue = getValue(secondGrandchildIndex);
		T thirdGrandchildValue = getValue(thirdGrandchildIndex);
		T fourthGrandchildValue = getValue(fourthGrandchildIndex);
		
		T maxGrandchild = getMaxGrandchild(firstGrandchildValue,
				secondGrandchildValue, thirdGrandchildValue,
				fourthGrandchildValue);
		
		if (maxGrandchild.compareTo(currValue) > 0) {
			int maxGrandchildIndex = 0;
			if (maxGrandchild == firstGrandchildValue) {
				maxGrandchildIndex = firstGrandchildIndex;
			} else if (maxGrandchild == secondGrandchildValue) {
				maxGrandchildIndex = secondGrandchildIndex;
			} else if (maxGrandchild == thirdGrandchildValue) {
				maxGrandchildIndex = thirdGrandchildIndex;
			} else {
				maxGrandchildIndex = fourthGrandchildIndex;
			}
			swapValuesAtIndices(maxGrandchildIndex, currIndex);
			currIndex = maxGrandchildIndex;
		}
		return currIndex;
	}

	/**
	 * Returns max among the four, accounting for null values.
	 * @param firstGrandchildValue
	 * @param secondGrandchildValue
	 * @param thirdGrandchildValue
	 * @param fourthGrandchildValue
	 * @return
	 */
	private T getMaxGrandchild(T firstGrandchildValue, T secondGrandchildValue,
			T thirdGrandchildValue, T fourthGrandchildValue) {
		
		// Compare first two, accounting for nulls
		T maxBetweenFirstTwoGrandchildren = secondGrandchildValue != null 
				&& firstGrandchildValue.compareTo(secondGrandchildValue) < 0 ? secondGrandchildValue
																			 : firstGrandchildValue;
		// Compare last two, accounting for nulls
		T maxBetweenLastTwoGrandchildren = thirdGrandchildValue == null ? null :
				fourthGrandchildValue != null 
				&& thirdGrandchildValue.compareTo(fourthGrandchildValue) < 0 ? fourthGrandchildValue
																	 	     : thirdGrandchildValue;
		// Max is max between first two and last two, accounting for nulls
		T maxGrandchild = maxBetweenLastTwoGrandchildren != null
				&& maxBetweenFirstTwoGrandchildren.compareTo(maxBetweenLastTwoGrandchildren) < 0 ?
						maxBetweenLastTwoGrandchildren : maxBetweenFirstTwoGrandchildren;
		return maxGrandchild;
	}
	
	/**
	 * Returns min among the four, accounting for null values.
	 * @param firstGrandchildValue
	 * @param secondGrandchildValue
	 * @param thirdGrandchildValue
	 * @param fourthGrandchildValue
	 * @return
	 */
	private T getMinGrandchild(T firstGrandchildValue, T secondGrandchildValue,
			T thirdGrandchildValue, T fourthGrandchildValue) {
		
		// Compare first two, accounting for nulls
		T maxBetweenFirstTwoGrandchildren = secondGrandchildValue != null 
				&& firstGrandchildValue.compareTo(secondGrandchildValue) > 0 ? secondGrandchildValue
																			 : firstGrandchildValue;
		// Compare last two, accounting for nulls
		T maxBetweenLastTwoGrandchildren = thirdGrandchildValue == null ? null :
				fourthGrandchildValue != null 
				&& thirdGrandchildValue.compareTo(fourthGrandchildValue) > 0 ? fourthGrandchildValue
																	 	     : thirdGrandchildValue;
		// Max is max between first two and last two, accounting for nulls
		T maxGrandchild = maxBetweenLastTwoGrandchildren != null
				&& maxBetweenFirstTwoGrandchildren.compareTo(maxBetweenLastTwoGrandchildren) > 0 ?
						maxBetweenLastTwoGrandchildren : maxBetweenFirstTwoGrandchildren;
		return maxGrandchild;
	}
	
	/**
	 * Returns true if this index has a child, or false if no children.
	 * @param index
	 * @return
	 */
	private boolean hasChildren(int index) {
		return getIndexOfLeftChild(index) != -1;
	}
	
	/**
	 * Returns elem  at current index, or null if this is outside the heap.
	 * @param index
	 * @return
	 */
	private T getValue(int index) {
		if (index > internalArray.size() - 1 || index == -1) {
			return null;
		}
		return internalArray.get(index);
	}
	
	private void bubbleUp(int index) {
		if (index == 0) return;
		Level currLevel = getLevelAtIndex(index);
		T valueAtCurrIndex = internalArray.get(index);
		int parentIndex = getParentIndex(index);
		
		// Case where we are on a Min level
		if (currLevel == Level.MIN) {
			
			// If parent is less, then swap. And then proceed to bubble up max.
			if (internalArray.get(parentIndex).compareTo(valueAtCurrIndex) < 0) {
				swapValuesAtIndices(parentIndex, index);
				bubbleUpMax(parentIndex);
			} 
			// If not, then just bubble up min to make sure min is at top.
			else {
				bubbleUpMin(index);
			}
		}
		// Case where we are on a Max level
		else {
			// If parent is greater, then swap. And then proceed to bubble up min.
			if (internalArray.get(parentIndex).compareTo(valueAtCurrIndex) > 0) {
				swapValuesAtIndices(parentIndex, index);
				bubbleUpMin(parentIndex);
			} 
			// If not, then just bubble up min to make sure min is at top.
			else {
				bubbleUpMax(index);
			}
		}
	}
	
	private void bubbleUpMax(int index) {
		while (index != 0) {
			int grandparentIndex = getParentIndex(getParentIndex(index));
			
			// If grandparent value < curr value, swap. This makes sure max value is at
			// the top of the heap.
			if (internalArray.get(grandparentIndex).compareTo(internalArray.get(index)) < 0) {
				swapValuesAtIndices(grandparentIndex, index);
				index = grandparentIndex;
			} else {
				return;
			}
		}
	}
	
	private void bubbleUpMin(int index) {
		while (index != 1 && index != 2) {
			int grandparentIndex = getParentIndex(getParentIndex(index));
			
			// If grandparent value < curr value, swap. This makes sure max value is at
			// the top of the heap.
			if (internalArray.get(grandparentIndex).compareTo(internalArray.get(index)) > 0) {
				swapValuesAtIndices(grandparentIndex, index);
				index = grandparentIndex;
			} else {
				return;
			}
		}
	}
	
	/**
	 * Swaps the values at given indices. Both must be within internalArray.
	 * @param first
	 * @param second
	 */
	private void swapValuesAtIndices(int first, int second) {
		T valueAtFirst = internalArray.get(first);
		T valueAtSecond = internalArray.get(second);
		internalArray.set(first, valueAtSecond);
		internalArray.set(second, valueAtFirst);
	}
	
	private Level getLevelAtIndex(int index) {
		int levelFromTop = (int)(Math.log(index + 1) / Math.log(2));
		
		// Even levels are max, odd levels are min.
		return levelFromTop % 2 == 0 ? Level.MAX : Level.MIN;
	}
	
	/**
	 * Returns index of left child, or -1 if child does not exist.
	 * @param currIndex
	 * @return
	 */
	private int getIndexOfLeftChild(int currIndex) {
		if (currIndex < 0) return -1;
		
		// We store the left child of any node at its index * 2, and its right child at index * 2 + 1
		int leftChildIndex = (currIndex + 1) * 2 - 1;
		
		// If this is outside the size of our internal array, then this means left child does not exist.
		if (internalArray.size() - 1 < leftChildIndex) {
			return -1;
		}
		return leftChildIndex;
	}
	
	/**
	 * Returns index of parent of this node, or -1 if the passed in node is the root.
	 * @param currIndex
	 * @return
	 */
	private int getParentIndex(int currIndex) {
		if (currIndex == 0) {
			return -1;
		}
		
		// Because we store left child at index * 2 and right child at index * 2 + 1,
		// the parent of any child will be at floor(log_2(child_index))
		return (currIndex + 1) / 2 - 1;
	}
	
	/**
	 * Example
	 * @param args
	 */
	public static void main(String[] args) { 
		MinMaxHeap<Integer> heap = new MinMaxHeap<Integer>(new Integer[]{1, 10, 100, 1000, 10000, 100000});

		System.out.println("Min: " + heap.removeMin());
		System.out.println("Max: " + heap.removeMax());
		System.out.println("Min: " + heap.removeMin());
		System.out.println("Max: " + heap.removeMax());
	}
}
