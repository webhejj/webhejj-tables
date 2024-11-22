package hu.webhejj.commons.io.table.csv;

import com.opencsv.CSVWriter;
import hu.webhejj.commons.io.table.TableReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;

public class CsvTableWriter {

    public void write(File csvFile, TableReader.Sheet sheet, Integer maxColumns) {
        if(maxColumns == null) {
            maxColumns = sheet.getRows().stream().mapToInt(TableReader.Row::size).max().orElse(0);
        }
        try (CsvTableAppender writer = new CsvTableAppender(csvFile)) {
            for (TableReader.Row row : sheet.getRows()) {
                for(int i = 0; i < Math.min(row.size(), maxColumns); i++) {
                    writer.append(row.getValue(i, String.class));
                }
                writer.newRow();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error while writing " + csvFile, e);
        }
    }
}
