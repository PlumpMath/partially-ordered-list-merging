package org.sautel.listmerging.order;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.google.common.collect.Ordering;

public class BufferedOrderedList<T extends Comparable<T>> implements
		OrderedList<T> {
	private final List<T> orderedValues;
	private int currentIndex;

	@SafeVarargs
	public BufferedOrderedList(T... values) {
		List<T> valuesList = newArrayList(values);
		orderedValues = Ordering.natural().sortedCopy(valuesList);
	}

	@Override
	public void consumeCurrentElement() {
		currentIndex++;
	}

	@Override
	public T getCurrentElement() {
		return orderedValues.get(currentIndex);
	}

	@Override
	public boolean hasCurrentElement() {
		return currentIndex < orderedValues.size();
	}
}
