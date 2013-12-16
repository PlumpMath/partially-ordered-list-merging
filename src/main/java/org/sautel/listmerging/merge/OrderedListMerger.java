package org.sautel.listmerging.merge;

import static com.google.common.collect.Collections2.filter;

import java.util.Collection;
import java.util.List;

import org.sautel.listmerging.order.OrderedList;

import com.google.common.base.Predicate;
import com.google.common.collect.Ordering;

public class OrderedListMerger<T extends Comparable<T>> {
	private static final class OrderedListOrdering<T extends Comparable<T>>
			extends Ordering<OrderedList<T>> {
		@Override
		public int compare(OrderedList<T> list1, OrderedList<T> list2) {
			return list1.getCurrentElement().compareTo(
					list2.getCurrentElement());
		}
	}

	private static final class ActiveInputListPredicate<T> implements
			Predicate<OrderedList<T>> {
		@Override
		public boolean apply(OrderedList<T> input) {
			return input.hasCurrentElement();
		}
	}

	private final ActiveInputListPredicate<T> activeInputListPredicate = new ActiveInputListPredicate<>();
	private final OrderedListOrdering<T> orderedListComparator = new OrderedListOrdering<>();

	public void merge(List<OrderedList<T>> inputLists, ListWriter<T> writer) {
		Collection<OrderedList<T>> activeInputLists = filterActiveInputLists(inputLists);
		while (!activeInputLists.isEmpty()) {
			consumeElementOnTheListWithMinElement(writer, activeInputLists);
			activeInputLists = filterActiveInputLists(inputLists);
		}
	}

	private Collection<OrderedList<T>> filterActiveInputLists(
			List<OrderedList<T>> inputLists) {
		return filter(inputLists, activeInputListPredicate);
	}

	private void consumeElementOnTheListWithMinElement(ListWriter<T> writer,
			Collection<OrderedList<T>> activeInputLists) {
		OrderedList<T> minInputList = orderedListComparator
				.min(activeInputLists);
		writer.write(minInputList.getCurrentElement());
		minInputList.consumeCurrentElement();
	}
}
