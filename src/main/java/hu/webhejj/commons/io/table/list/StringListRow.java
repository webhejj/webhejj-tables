/*
 *  Copyright Gergely Nagy <greg@webhejj.hu>
 *
 *  Licensed under the Apache License, Version 2.0;
 *  you may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package hu.webhejj.commons.io.table.list;

import hu.webhejj.commons.io.table.TableUtils;
import hu.webhejj.commons.io.table.AbstractTableRow;

import java.lang.Boolean;import java.lang.Class;import java.lang.ClassCastException;import java.lang.Enum;import java.lang.Integer;import java.lang.Long;import java.lang.Override;import java.lang.String;import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class StringListRow extends AbstractTableRow {

    protected final List<String> row;

    public StringListRow(List<String> row) {
        this.row = row;
    }

    public StringListRow(String... row) {
        this.row = Arrays.asList(row);
    }

    @Override
    public int size() {
        return row.size();
    }

    @Override
    public <T> T getValue(int columnIndex, Class<T> valueType) {
        String stringValue = row.get(columnIndex);

        // TODO: move this to a generic type coercer
        if(String.class.isAssignableFrom(valueType)) {
            return (T) stringValue;

        } else if(Integer.class.isAssignableFrom(valueType)) {
            return (T) Integer.valueOf(stringValue);

        } else if(Long.class.isAssignableFrom(valueType)) {
            return (T) Long.valueOf(stringValue);

        } else if(valueType.isEnum()) {
            return (T) Enum.valueOf((Class<? extends Enum>) valueType, stringValue);

        } else if(BigDecimal.class.isAssignableFrom(valueType)) {
            return (T) (TableUtils.isEmpty(stringValue) ? null : new BigDecimal(stringValue));

        } else if(Boolean.class.isAssignableFrom(valueType)) {
            return (T) Boolean.valueOf("true".equalsIgnoreCase(stringValue)
                    || (!TableUtils.isEmpty(stringValue) && !"0".equals(stringValue)));

        } else {
            throw new ClassCastException("Can't convert " + stringValue + " to " + valueType.getName());
        }
    }
}
