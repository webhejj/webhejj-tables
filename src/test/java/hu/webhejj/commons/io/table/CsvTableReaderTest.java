/*
 *  Copyright Gergely Nagy <greg@webhejj.hu>
 *
 *  Licensed under the Apache License, Version 2.0;
 *  you may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */

package hu.webhejj.commons.io.table;

import hu.webhejj.commons.io.table.csv.CsvTableReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CsvTableReaderTest {

    @Test
    public void testCsvTableReader() {

        String name = "test.csv";
        String csv = "a,b,c\n1,2,\"3\"";
        CsvTableReader csvTableReader = new CsvTableReader(name, new StringReader(csv), ',', '"');
        Assert.assertEquals("sheet count", 1, csvTableReader.getSheets().size());
        Assert.assertEquals("sheet name", name, csvTableReader.getSheet(0).getName());

        TableReader.Sheet sheet = csvTableReader.getSheet(name);
        Assert.assertNotNull("get sheet by name", sheet);
        Assert.assertEquals("row lenght", 3, sheet.getRow(0).size());
        Assert.assertEquals("read string from A1", "a", sheet.getRow(0).getValue(0, String.class));
        Assert.assertEquals("read int from A2", Integer.valueOf(1), sheet.getRow(1).getValue(0, Integer.class));
    }
}
