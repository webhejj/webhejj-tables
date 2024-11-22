/*
 *  Copyright Gergely Nagy <greg@webhejj.hu>
 *
 *  Licensed under the Apache License, Version 2.0;
 *  you may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package hu.webhejj.commons.io.table;

import hu.webhejj.commons.io.table.list.RowListSheet;
import hu.webhejj.commons.io.table.list.StringListRow;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TableUtils {
    /**
     * @return true if specified value is null, empty string or containing only whitespace or empty collection
     */
    public static boolean isEmpty(Object o) {

        if (o == null) {
            return true;
        }

        if (o instanceof String) {
            return ((String) o).isBlank();

        } else if (o instanceof Collection<?>) {
            return ((Collection<?>) o).size() == 0;
        }

        return false;
    }

    /**
     * Drops trailing blank cells from the end of rows and trailing blank rows from the end of the sheet
     *
     * @param sheet sheet to process
     * @return sheet with all values converted to string
     */
    public static RowListSheet dropTrailingBlankCells(TableReader.Sheet sheet) {
        List<TableReader.Row> convertedRows = sheet.getRows().stream()
                .map(row -> new StringListRow(
                        row.getStringValues().stream()
                                .dropWhile(value -> value == null || value.isBlank())
                                .collect(Collectors.toList())
                ))
                .dropWhile(row -> row.getStringValues().stream().allMatch(value -> value == null || value.isBlank()))
                .collect(Collectors.toList());

        return new RowListSheet(sheet.getIndex(), sheet.getName(), convertedRows);
    }
}
