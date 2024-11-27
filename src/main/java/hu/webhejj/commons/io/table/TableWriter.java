package hu.webhejj.commons.io.table;

import hu.webhejj.commons.io.table.csv.CsvTableAppender;
import hu.webhejj.commons.io.table.excel.ExcelTableAppender;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public class TableWriter {

    public void writeCsv(File csvFile, TableReader.Sheet sheet, Integer maxColumns) {
        try {
            writeCsv(() -> new CsvTableAppender(csvFile), sheet, maxColumns);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing " + csvFile, e);
        }
    }

    public void writeExcel(File excelFile, TableReader.Sheet sheet, Integer maxColumns) {
        try {
            writeCsv(() -> new ExcelTableAppender(excelFile), sheet, maxColumns);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing " + excelFile, e);
        }
    }

    public void writeCsv(Supplier<TableAppender> appenderSupplier, TableReader.Sheet sheet, Integer maxColumns) throws IOException {

        if (maxColumns == null) {
            maxColumns = sheet.getRows().stream().mapToInt(TableReader.Row::size).max().orElse(0);
        }
        try (TableAppender writer = appenderSupplier.get()) {
            for (TableReader.Row row : sheet.getRows()) {
                for (int i = 0; i < Math.min(row.size(), maxColumns); i++) {
                    writer.append(row.getValue(i, String.class));
                }
                writer.newRow();
            }
        }
    }
}
