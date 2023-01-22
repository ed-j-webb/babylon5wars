package aog.b5w.utils;

import java.util.Iterator;

/**
 * An implementation of the Iterator interface that can count round the numbers on a clock face
 * @author Ed Webb
 *
 */
public class ClockIterator implements Iterator<Integer> {

	private Integer max = 11;
	private Integer start;
	private Integer end;
	private Integer current;
	
	public ClockIterator(Integer start, Integer end) {
		this.start = start;
		this.end = end;
	}
	
	public ClockIterator(Integer size, Integer start, Integer end) {
		this(start, end);
		this.max = size;
	}
	
	@Override
	public boolean hasNext() {
		if (current == null) {
			return true;
		} else {
			return (current != end);
		}
	}

	@Override
	public Integer next() {
		if (current == null) {
			current = start;
		} else if (current.equals(max)) {
			current = Integer.valueOf(0);
		} else {
			current = current + 1;
		}
		return current;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("You cannot remove items from this Iterator");
	}

	
	public static void main (String[] args) {
		iterate(new ClockIterator(3, 6));
		iterate(new ClockIterator(8, 3));
		iterate(new ClockIterator(5, 5));
		
	}
	
	public static void iterate(Iterator<Integer> it) {
		while (it.hasNext()) {
			System.out.println(it.next());
		}
		System.out.println();
	}
	
}
