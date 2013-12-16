package org.sautel.listmerging.order;

import static java.util.Collections.min;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PartiallyOrderedList<T extends Comparable<T>> implements
		OrderedList<T> {
	private final Iterator<T> iterator;
	private final List<T> bufferedValues;

	public PartiallyOrderedList(Iterator<T> iterator, int maxPositionError) {
		this.iterator = iterator;
		int bufferSize = maxPositionError + 1;
		bufferedValues = new ArrayList<>(bufferSize);
		for (int i = 0; i < bufferSize; i++) {
			addNextValueIfExists();
		}
	}

	private void addNextValueIfExists() {
		if (iterator.hasNext()) {
			bufferedValues.add(iterator.next());
		}
	}

	@Override
	public void consumeCurrentElement() {
		T currentValue = getCurrentElement();
		int currentValueIndex = bufferedValues.indexOf(currentValue);
		bufferedValues.remove(currentValueIndex);
		addNextValueIfExists();
	}

	@Override
	public T getCurrentElement() {
		return min(bufferedValues);
	}

	@Override
	public boolean hasCurrentElement() {
		return !bufferedValues.isEmpty();
	}
}
