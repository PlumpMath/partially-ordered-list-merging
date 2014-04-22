package org.sautel.listmerging.order;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Comparator.naturalOrder;

public class BufferedOrderedList<T extends Comparable<T>> implements
        OrderedList<T> {
    private final List<T> orderedValues;
    private int currentIndex;

    @SafeVarargs
    public BufferedOrderedList(T... values) {
        orderedValues = newArrayList(values);
        orderedValues.sort(naturalOrder());
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
