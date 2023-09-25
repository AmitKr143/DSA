package com.dsa;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

public class ArrayListTest<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable{
	
	private static final long serialVerionUID = 2l;
	
	//Default initial capacity
	private static final int DEFAULT_CAPACITY = 10;
	
	//shared empty array instance used for empty instances
	private static final Object[] EMPTY_ELEMENTDATA = {};
	
	/*
	 * shared empty array instance used for default size empty instances. we
	 * distinguish this from EMPTY_ELEMENTDATA to know how much to inflate when
	 * first element is added. 
	 */
	private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
	
	/*
	 * The array buffer into which the elements of the ArrayList would be stored.
	 * the capacity of the ArrayList is the length of this array buffer. Any
	 * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
	 * will be expanded to DAFAULT_CAPACITY when the first element is added.
	 */
	transient Object[] elementData;
	
	//size of the Array
	private int size;
	
	/*
	 * constructs the empty list with the specified initial capacity.
	 * 
	 * @Param initialCapacity is the  initial capacity of the list.
	 * throws IllegalArumentException if the specified initial capacity is negative. 
	 */
	public ArrayListTest( int initialCapacity) {
		if(initialCapacity > 0) {
			this.elementData = new Object [initialCapacity];
		} else if(initialCapacity == 0) {
			this.elementData = EMPTY_ELEMENTDATA;
		} else {
			throw new IllegalArgumentException("illegal capacity "+initialCapacity);
		}
	}
	
	// constructs a empty list with an initial capacity of 10;
	public ArrayListTest() {
		this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
	}
	
	/*
	 * constructs a list containing the elements of the specified collections.
	 * in the order they are returned by the collectio's iterator.
	 * 
	 * @Param c the collection whose elements are to be placed in the specified list.
	 * throws NullPointerException if the specified collection is null.
	 */
	public ArrayListTest(Collection<? extends E> c ) {
		elementData = c.toArray();
		if( (size = elementData.length) != 0) {
			if(elementData.getClass() != Object[].class) {
				elementData = Arrays.copyOf(elementData, size, Object[].class);
			}
		} else {
			this.elementData = EMPTY_ELEMENTDATA;
		}
	}
	
	/*
	 * Trims the capacity of this ArrayList to the list current size.
	 * An application can use this operation to minimize the storage of the instance.
	 */
	public void trimToSize() {
		modCount ++;
		if(size < elementData.length) {
			elementData = (size == 0) ? EMPTY_ELEMENTDATA : Arrays.copyOf(elementData, size);
		}
	}
	
	/*
	 * Increases the capacity of this ArrayList if necessary.
	 * ensures that it can hold at least the number of elements
	 * specified by the minimum capacity argument.
	 * @Param minimumCapacity, the desired minimum capacity.
	 */
	public void ensureCapacity(int minimumCapacity) {
		if(minimumCapacity > elementData.length 
				&& !(elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA && minimumCapacity <= DEFAULT_CAPACITY)) {
			modCount ++;
			grow(minimumCapacity);
		}
	}

	/*
	 * Increases the capacity to ensure can hold at least the number
	 * of elements mentioned in the specified by the minimum capacity argument.
	 * 
	 * @Param minimumCapacity the desired minimum capacity.
	 * throws OutOfMemory Exception if minCapacity is less that 0.
	 */
	private Object[] grow(int minimumCapacity) {
		int oldCapacity = elementData.length;
		if(oldCapacity > 0 || elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
			int newCapacity = newLength(oldCapacity, minimumCapacity - oldCapacity, oldCapacity >> 1);
			return elementData = Arrays.copyOf(elementData, newCapacity);
		} else {
			return elementData = new Object[Math.max(DEFAULT_CAPACITY, minimumCapacity)];
		}
		
	}

	private static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE -8;
	
	private int newLength(int oldCapacity, int minGrowth, int prefGrowth) {
		int prefLength = oldCapacity + Math.max(minGrowth, prefGrowth);
		if(0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH) {
			return prefLength;
		} else {
			return hugeLength ( oldCapacity, minGrowth);
		}
	}

	private int hugeLength(int oldLength, int minGrowth) {
		int minLength = oldLength + minGrowth;
		if(minLength < 0) {
			throw new OutOfMemoryError("Required Array length "+oldLength +" + "+ minGrowth +" is too large");
		} else if(minLength <= SOFT_MAX_ARRAY_LENGTH) {
			return SOFT_MAX_ARRAY_LENGTH;
		} else {
			return minLength;
		}
	}
	
	private Object[] grow() {
		return grow(size +1);
	}

	@Override
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public boolean contains(Object o) {
		return indexOf(o) >= 0;
	}
	
	public int indexOf(Object o) {
		return indexOfRange(o, 0, size);	
	}
	
	int indexOfRange(Object o, int start, int end) {
		Object[] es = elementData;
		if(o == null) {
			for(int i=0; i<size; i++) {
				if(es[i] == null)
					return i;
			}
		} else {
			for(int i=0; i<size; i++) {
				if(o.equals(es[i]))
					return i;
			}
		}
		return -1;
	}
	
	public int lastIndexof(Object o) {
		return lastIndexOfRange(o, 0, size);
	}
	
	int lastIndexOfRange(Object o, int start, int end) {
		Object[] es = elementData;
		if(o == null) {
			for(int i=end-1; i>=start; i--) {
				if(es[i] == null)
					return i;
			}
		} else {
			for(int i= end -1; i>=start; i--) {
				if(o.equals(es[i]))
					return i;
			}
		}
		
		return -1;
	}
	
	public Object clone() {
		try {
			ArrayListTest<?> clone = (ArrayListTest<?>) super.clone();
			clone.elementData = Arrays.copyOf(elementData, size);
			clone.modCount = 0;
			return clone;
		} catch(CloneNotSupportedException ex) {
			throw new InternalError(ex);
		}
	}
	
	public Object[] toArray() {
		return Arrays.copyOf(elementData, size);
	}
	
	public <T> T[] toArray(T[] a) {
		if(a.length < size)
			return (T[]) Arrays.copyOf(elementData, size, a.getClass());
		
		System.arraycopy(elementData, 0, a, 0, size);
		if(a.length > size) {
			a[size] = null;
		}
		
		return a;
	}
	
	E elementData(int index) {
		return (E) elementData[index];
	}
	
	static <E> E elementAt(Object[] es, int index) {
		return (E) es[index];
	}
	
	@Override
	public E get(int index) {
		Objects.checkIndex(	index, size);
		return elementData(index);
	}
	
	public E set(int index, E element) {
		Objects.checkIndex(index, size);
		E oldValue = elementData(index);
		elementData[index] = element;
		return oldValue;
	}
	
	private void add(E e, Object[] elementData, int size) {
		if(size == elementData.length) {
			elementData = grow();
		}
		elementData[size] = e;
		size = size +1;
	}
	
	public boolean add(E e) {
		modCount++;
		add(e, elementData, size);
		return true;
	}
	
	public void add(int index, E element) {
		if(index > size || index < 0)
			throw new IndexOutOfBoundsException("Position is out of bounds "+index);
		modCount++;
		final int s;
		Object[] elementData;
		if((s = size) == (elementData = this.elementData).length) {
			elementData = grow();
		}
		System.arraycopy(elementData, index, elementData, index+1, s-index);
		elementData[index] = element;
		size = s+1;
	}
	
	

}
