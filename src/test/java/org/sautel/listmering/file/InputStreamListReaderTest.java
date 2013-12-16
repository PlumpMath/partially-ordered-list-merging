package org.sautel.listmering.file;

import static com.google.common.base.Joiner.on;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.sautel.listmerging.file.InputStreamListReader;

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
			String file = on("\n").join(lines);
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
