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

import java.lang.Override;import java.lang.String;import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SheetListTableReader implements TableReader {

    private List<Sheet> sheets;

    public SheetListTableReader(List<Sheet> sheets) {
        this.sheets = sheets;
    }
    public SheetListTableReader(Sheet... sheets) {
        this.sheets = Arrays.asList(sheets);
    }

    @Override
    public Sheet getSheet(int index) {
        if(index < 0 || index >= sheets.size())
            return null;
        else {
           return sheets.get(index);
        }
    }

    @Override
    public Sheet getSheet(String name) {
        for(Sheet sheet: sheets) {
            if(Objects.equals(name, sheet.getName())) {
                return sheet;
            }
        }
        return null;
    }

    @Override
    public List<Sheet> getSheets() {
        return sheets;
    }
    public void setSheets(List<Sheet> sheets) {
        this.sheets = sheets;
    }
}
