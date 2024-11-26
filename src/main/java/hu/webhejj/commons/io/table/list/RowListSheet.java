/*
 *  Copyright Gergely Nagy <greg@webhejj.hu>
 *
 *  Licensed under the Apache License, Version 2.0;
 *  you may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package hu.webhejj.commons.io.table.list;

import hu.webhejj.commons.io.table.TableReader;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RowListSheet implements TableReader.Sheet {

    private int sheetIndex;
    private String name;
    private List<TableReader.Row> rows;

    public RowListSheet(int sheetIndex, String name, TableReader.Row... rows) {
        this(sheetIndex, name, Arrays.asList(rows));
    }

    public RowListSheet(int sheetIndex, String name, List<TableReader.Row> rows) {
        this.sheetIndex = sheetIndex;
        this.name = name;
        this.rows = rows;
    }

    @Override
    public int getIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public TableReader.Row getRow(int index) {
        return rows.get(index);
    }

    @Override
    public List<TableReader.Row> getRows() {
        return rows;
    }

    public void setRows(List<TableReader.Row> rows) {
        this.rows = rows;
    }

    @Override
    public Iterator<TableReader.Row> iterator() {
        return rows.iterator();
    }

    public static RowListSheet fromStrings(int sheetIndex, String name, List<List<String>> values) {
        List<TableReader.Row> list = values.stream()
                .map(line -> (TableReader.Row) new StringListRow(line))
                .toList();
        return new RowListSheet(sheetIndex, name, list);
    }
}
