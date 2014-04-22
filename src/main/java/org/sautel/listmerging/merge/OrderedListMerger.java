package org.sautel.listmerging.merge;

import org.sautel.listmerging.order.OrderedList;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class OrderedListMerger<T extends Comparable<T>> {
    public void merge(List<OrderedList<T>> inputLists, ListWriter<T> writer) {
        Collection<OrderedList<T>> activeInputLists = filterActiveInputLists(inputLists);
        while (!activeInputLists.isEmpty()) {
            consumeElementOnTheListWithMinElement(writer, activeInputLists);
            activeInputLists = filterActiveInputLists(inputLists);
        }
    }

    private Collection<OrderedList<T>> filterActiveInputLists(
            List<OrderedList<T>> inputLists) {
        return inputLists.stream().filter(OrderedList::hasCurrentElement).collect(toList());
    }

    private void consumeElementOnTheListWithMinElement(ListWriter<T> writer,
                                                       Collection<OrderedList<T>> activeInputLists) {
        Optional<OrderedList<T>> optionalMinInputList = activeInputLists.stream()
                .min(comparing(OrderedList::getCurrentElement));
        if (optionalMinInputList.isPresent()) {
            OrderedList<T> minInputList = optionalMinInputList.get();
            writer.write(minInputList.getCurrentElement());
            minInputList.consumeCurrentElement();
        } else {
            throw new IllegalStateException("No input list");
        }
    }
}
