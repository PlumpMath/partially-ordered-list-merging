package org.sautel.listmering.file;

import org.junit.Test;
import org.sautel.listmerging.file.InputStreamListReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InputStreamListReaderTest {
	@Test
	public void emptyFile() {
		InputStreamListReader iterator = buildStreamListReader();

		assertFalse(iterator.hasNext());
	}

	@Test
	public void oneLineFile() {
		InputStreamListReader iterator = buildStreamListReader("123");

		assertTrue(iterator.hasNext());
		assertEquals(123, (int) iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void twoLinesFile() {
		InputStreamListReader iterator = buildStreamListReader("123", "456");

		assertTrue(iterator.hasNext());

		assertEquals(123, (int) iterator.next());
		assertTrue(iterator.hasNext());

		assertEquals(456, (int) iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unsupportedRemoveOperation() {
		InputStreamListReader iterator = buildStreamListReader("123", "456");

		iterator.remove();
	}

	private InputStreamListReader buildStreamListReader(String... lines) {
		InputStream inputStream = buildInputStream(lines);
		return new InputStreamListReader(inputStream);
	}

	private InputStream buildInputStream(String... lines) {
		try {
            String file = asList(lines).stream().collect(joining("\n"));
			return new ByteArrayInputStream(file.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}



    @Test
	public void ensureStreamIsClosed() throws IOException {
		InputStream stream = spy(buildInputStream("123"));
		InputStreamListReader iterator = new InputStreamListReader(stream);

		verify(stream, never()).close();
		iterator.next();

		verify(stream, times(1)).close();
	}
}
