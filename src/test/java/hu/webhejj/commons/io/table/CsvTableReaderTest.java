package hu.webhejj.commons.io.table;

import hu.webhejj.commons.io.table.csv.CsvTableReader;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class CsvTableReaderTest {

    @Test
    public void testCsvTableReader() {

        String name = "test.csv";
        String csv = "a,b,c\n1,2,\"3\"";
        CsvTableReader csvTableReader = new CsvTableReader(name, new StringReader(csv), ',', '"');
        assertEquals(1, csvTableReader.getSheets().size(), "sheet count");
        assertEquals(name, csvTableReader.getSheet(0).getName(), "sheet name");

        TableReader.Sheet sheet = csvTableReader.getSheet(name);
        assertNotNull(sheet, "get sheet by name");
        assertEquals(3, sheet.getRow(0).size(), "row length");
        assertEquals("a", sheet.getRow(0).getValue(0, String.class), "read string from A1");
        assertEquals(Integer.valueOf(1), sheet.getRow(1).getValue(0, Integer.class), "read int from A2");
    }
}
