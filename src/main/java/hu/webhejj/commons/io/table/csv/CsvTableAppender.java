/*
 *  Copyright Gergely Nagy <greg@webhejj.hu>
 *
 *  Licensed under the Apache License, Version 2.0;
 *  you may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package hu.webhejj.commons.io.table.csv;

import hu.webhejj.commons.io.table.TableUtils;
import hu.webhejj.commons.io.table.TableAppender;

import java.io.*;

/**
 *
 * TableAppender for comma separated format
 *
 * @author Gergely Nagy <greg@webhejj.hu>
 *
 */
public class CsvTableAppender implements TableAppender, AutoCloseable {

	private final Writer writer;
	private int columnCount = 0;

	public CsvTableAppender(File outputFile) {
        try {
            this.writer = new FileWriter(outputFile);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

	public CsvTableAppender(Writer writer) {
		this.writer = writer;
	}

	@Override
	public void append(String value) {
		try {
			if(columnCount++ > 0) {
					writer.write(",");
			}

			if(TableUtils.isEmpty(value)) {
				return;
			}

			boolean escaped = needsEscaping(value);

			if(escaped) {
				writer.write('"');
				for(int i = 0; i < value.length(); i++) {
					char c = value.charAt(i);
					if(c == '"') {
						writer.write("\\");
					}
					writer.write(c);
				}
				writer.write('"');

			} else {
				writer.write(value);
			}

		} catch (IOException e) {
			throw new UncheckedIOException("Error while writing " + value, e);
		}
	}

	@Override
	public void newRow() {
		try {
			writer.write("\n");
		} catch (IOException e) {
			throw new UncheckedIOException("Error while writing newline character", e);
		}
		columnCount = 0;
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	protected boolean needsEscaping(String value) {
		return value.contains(",")
				|| value.contains(" ")
				|| value.contains("\\")
				|| value.contains("\"")
				|| value.contains("\r")
				|| value.contains("\n");
	}
}
