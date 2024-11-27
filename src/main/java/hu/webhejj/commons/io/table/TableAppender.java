/*
 *  Copyright Gergely Nagy <greg@webhejj.hu>
 *
 *  Licensed under the Apache License, Version 2.0;
 *  you may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package hu.webhejj.commons.io.table;

import java.io.IOException;

/**
 * Interface for appending tabular data
 *
 * @author greg
 *
 */
public interface TableAppender extends AutoCloseable{

	/**
	 * Append value to the table, moving the "cursor" to the next cell in the row.
	 *
	 * @param value value to append
	 */
	void append(String value);

	/**
	 * End previous row and move "cursor" to beginning of new row.
	 *
	 */
	void newRow();

	@Override
	default void close() throws IOException {
		// Default implementation does nothing
	}
}
