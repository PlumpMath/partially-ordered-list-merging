package org.sautel.listmerging.file;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;


public class InputStreamListReader implements Iterator<Integer> {
	private final BufferedReader reader;
	private Optional<Integer> currentValue = empty();
	private Optional<Integer> nextValue = empty();

	public InputStreamListReader(InputStream stream) {
		reader = new BufferedReader(new InputStreamReader(stream));
		readNewLine();
	}

	@Override
	public boolean hasNext() {
		return nextValue.isPresent();
	}

	@Override
	public Integer next() {
		readNewLine();
		if (currentValue.isPresent()) {
			return currentValue.get();
		}
		throw new IllegalStateException("No next element available");

	}

	private void readNewLine() {
		Optional<Integer> line = readLineIfAvailable();
		currentValue = nextValue;
		nextValue = line;
	}

	private Optional<Integer> readLineIfAvailable() {
		try {
			String line = reader.readLine();
			if (line != null) {
				return of(Integer.valueOf(line));
			}
			reader.close();
			return empty();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
