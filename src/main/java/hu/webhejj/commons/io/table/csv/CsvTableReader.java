/*
 *  Copyright Gergely Nagy <greg@webhejj.hu>
 *
 *  Licensed under the Apache License, Version 2.0;
 *  you may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */

package hu.webhejj.commons.io.table.csv;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import hu.webhejj.commons.io.table.list.RowListSheet;
import hu.webhejj.commons.io.table.list.SheetListTableReader;
import hu.webhejj.commons.io.table.list.StringListRow;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CsvTableReader extends SheetListTableReader {

    public CsvTableReader(String name, Reader reader, char separator, char quote) {
        super(openCsv(name, reader, separator, quote));
    }

    private static Sheet openCsv(String name, Reader reader, char separator, char quote) {
        try (CSVReader csvReader = csvReader(reader, separator, quote)) {
            List<String[]> csvRows = csvReader.readAll();
            List<Row> rows = new ArrayList<>(csvRows.size());
            for (String[] csvRow : csvRows) {
                rows.add(new StringListRow(csvRow));
            }
            return new RowListSheet(0, name, rows);

        } catch (IOException | CsvException e) {
            throw new RuntimeException("Error while reading " + name, e);
        }
    }

    private static CSVReader csvReader(Reader reader, char separator, char quote) {
        return new CSVReaderBuilder(reader)
                .withCSVParser(
                        new CSVParserBuilder()
                                .withSeparator(separator)
                                .withQuoteChar(quote).build()
                )
                .build();
    }
}
