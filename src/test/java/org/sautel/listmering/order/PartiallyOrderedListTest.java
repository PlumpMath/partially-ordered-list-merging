package org.sautel.listmering.order;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.sautel.listmerging.order.PartiallyOrderedList;

public class PartiallyOrderedListTest {
	@Test
	public void emptyList() {
		PartiallyOrderedList<Integer> list = buildPartiallyOrderedList(3);

		assertFalse(list.hasCurrentElement());
	}

	private PartiallyOrderedList<Integer> buildPartiallyOrderedList(
			int maxPositionError, Integer... values) {
		List<Integer> valuesList = newArrayList(values);
		return new PartiallyOrderedList<>(valuesList.iterator(),
				maxPositionError);
	}

	@Test
	public void oneElementList() {
		PartiallyOrderedList<Integer> list = buildPartiallyOrderedList(3, 1);

		assertValuesAre(list, 1);
	}

	private void assertValuesAre(PartiallyOrderedList<Integer> list,
			int... expectedValues) {
		for (int expectedValue : expectedValues) {
			assertTrue(list.hasCurrentElement());
			assertEquals(expectedValue, (int) list.getCurrentElement());
			list.consumeCurrentElement();
		}
		assertFalse(list.hasCurrentElement());
	}

	@Test
	public void twoOrderedElementsList() {
		PartiallyOrderedList<Integer> list = buildPartiallyOrderedList(3, 1, 2);

		assertValuesAre(list, 1, 2);
	}

	@Test
	public void twoUnorderedElementsListWithAnAcceptedError() {
		PartiallyOrderedList<Integer> list = buildPartiallyOrderedList(1, 2, 1);

		assertValuesAre(list, 1, 2);
	}

	@Test
	public void threeUnorderedElementsListWithANotAcceptedError() {
		PartiallyOrderedList<Integer> list = buildPartiallyOrderedList(1, 3, 2,
				1);

		assertValuesAre(list, 2, 1, 3);
	}

	@Test
	public void fiveOrderedElementsList() {
		PartiallyOrderedList<Integer> list = buildPartiallyOrderedList(3, 1, 2,
				3, 4, 5);

		assertValuesAre(list, 1, 2, 3, 4, 5);
	}

	@Test
	public void fiveOrderedElementsListWithAnAcceptedError() {
		PartiallyOrderedList<Integer> list = buildPartiallyOrderedList(2, 3, 2,
				1, 4, 5);

		assertValuesAre(list, 1, 2, 3, 4, 5);
	}

	@Test
	public void fiveOrderedElementsListWithNotAcceptedError() {
		PartiallyOrderedList<Integer> list = buildPartiallyOrderedList(2, 3, 2,
				5, 4, 1);

		assertValuesAre(list, 2, 3, 1, 4, 5);
	}
}
