/*
 *  Copyright Gergely Nagy <greg@webhejj.hu>
 *
 *  Licensed under the Apache License, Version 2.0;
 *  you may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package hu.webhejj.commons.io.table;

import java.lang.Integer;
import java.lang.String;

public class CellAddress {
    private String columnAlpha;
    private Integer columnIndex;
    private Integer rowIndex;

    public CellAddress() {
    }

    public CellAddress(Integer columnIndex, Integer rowIndex) {
        this.columnAlpha = columnIndex == null ? null : AlphaColumnUtil.toAlpha(columnIndex);
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
    }

    public CellAddress(String columnAlpha, Integer rowIndex) {
        this.columnAlpha = columnAlpha;
        this.columnIndex = columnAlpha == null ? null : AlphaColumnUtil.toNumeric(columnAlpha);
        this.rowIndex = rowIndex;
    }

    public String getColumnAlpha() {
        return columnAlpha;
    }

    public void setColumnAlpha(String columnAlpha) {
        this.columnAlpha = columnAlpha;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override
    public String toString() {
        if (columnAlpha != null && rowIndex != null) {
            return columnAlpha + rowIndex;
        } else if (rowIndex == null) {
            return columnAlpha;
        } else if (columnAlpha == null) {
            return Integer.toString(rowIndex);
        }
        return "";
    }
}
