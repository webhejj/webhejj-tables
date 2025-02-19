/*
 *  Copyright Gergely Nagy <greg@webhejj.hu>
 *
 *  Licensed under the Apache License, Version 2.0;
 *  you may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package hu.webhejj.commons.io.table.excel;

import hu.webhejj.commons.io.table.TableUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.math.BigDecimal;
import java.text.Format;

public class ExcelRowValueConverter {

	private FormulaEvaluator evaluator;
	private DataFormatter formatter;

	public ExcelRowValueConverter(FormulaEvaluator evaluator) {
		this.evaluator = evaluator;
		formatter = new DataFormatter();
	}

	public <T> T getValue(Row row, int column, Class<T> valueType) {

		if (row == null) {
			return null;
		}

		Cell cell = row.getCell(column);
		if (cell == null) {
			return null;
		}

		CellValue cellValue = getCellValue(row, cell, column);
		if (cellValue == null) {
			return null;
		}

		CellType cellType = cell.getCellType().equals(CellType.FORMULA) ? cell.getCachedFormulaResultType() : cell.getCellType();

		switch (cellType) {
			case BLANK:
				return null;

			case BOOLEAN:
				if (String.class.isAssignableFrom(valueType)) {
					return (T) Boolean.toString(cellValue.getBooleanValue());

				} else if (Integer.class.isAssignableFrom(valueType)) {
					return (T) Integer.valueOf(cellValue.getBooleanValue() ? 1 : 0);

				} else if (Long.class.isAssignableFrom(valueType)) {
					return (T) Long.valueOf(cellValue.getBooleanValue() ? 1L : 0L);

				} else {
					throw new ClassCastException("Can't convert " + cellValue.getBooleanValue() + " to " + valueType.getName());
				}

			case STRING:
				String stringValue = cellValue.getStringValue();
				if (TableUtils.isEmpty(stringValue)) {
					return null;
				}
				if ("null".equals(stringValue)) {
					return null;
				}
				if (String.class.isAssignableFrom(valueType)) {
					return (T) stringValue;

				} else if (Integer.class.isAssignableFrom(valueType)) {
					return (T) Integer.valueOf(stringValue);

				} else if (Long.class.isAssignableFrom(valueType)) {
					return (T) Long.valueOf(stringValue);

				} else if (valueType.isEnum()) {
					return (T) Enum.valueOf((Class<? extends Enum>) valueType, stringValue);

				} else if (BigDecimal.class.isAssignableFrom(valueType)) {
					return (T) (TableUtils.isEmpty(stringValue) ? null : new BigDecimal(stringValue));

				} else if (Boolean.class.isAssignableFrom(valueType)) {
				return (T) Boolean.valueOf("true".equalsIgnoreCase(stringValue) || (!TableUtils.isEmpty(stringValue) && !"0".equals(stringValue)));

				} else {
					throw new ClassCastException("Can't convert " + stringValue + " to " + valueType.getName());
				}

			case NUMERIC:
				if (String.class.isAssignableFrom(valueType)) {

					// TODO: time zones?
                    if(DateUtil.isCellDateFormatted(cell)) {
                        return (T) cell.getLocalDateTimeCellValue().toString();
                    }

					Format format = formatter.createFormat(cell);
					if (format == null) {
						// TODO: do this without creating a BigDecimal each time
						return (T) new BigDecimal(cellValue.getNumberValue()).toString();
					} else {
						return (T) format.format(cellValue.getNumberValue());
					}

				} else if (Integer.class.isAssignableFrom(valueType)) {
					return (T) Integer.valueOf((int) cellValue.getNumberValue());

				} else if (Long.class.isAssignableFrom(valueType)) {
					return (T) Long.valueOf((int) cellValue.getNumberValue());

				} else {
					throw new ClassCastException("Can't convert " + cellValue.getNumberValue() + " to " + valueType.getName());
				}

			case ERROR:
				FormulaError error = FormulaError.forInt(cell.getErrorCellValue());
				if (FormulaError.NA.equals(error)) {
					return null;
                } else {
                    if (String.class.isAssignableFrom(valueType)) {
                        return (T) error.getString();
					} else {
						// System.err.format("  Cell[%d,%d] error code %s\n", r.getRowNum(), column, error);
                        throw new RuntimeException(String.format("Cell[%d,%d] error code %s", row.getRowNum(), column, error));
                    }
				}
		}
		throw new IllegalArgumentException("Don't know how to convert cell of type " + cellValue.getCellType());
	}

	public boolean isNumber(Cell cell) {
		if (cell == null) {
			return false;
		}
		switch (cell.getCellType()) {
			case BLANK:
				return false;
			case BOOLEAN:
				return false;
			case ERROR:
				return false;
			case FORMULA:
				return false; // TODO...
			case NUMERIC:
				return true;
			case STRING:
				return false; // TODO...
		}
		return false;
	}

	private CellValue getCellValue(Row row, Cell cell, int column) {

		CellValue cellValue = null;
		try {
			cellValue = evaluator.evaluate(cell);
		} catch (RuntimeException e) {
			if (cell.getCellType() == CellType.FORMULA) {
				switch (cell.getCachedFormulaResultType()) {
					case NUMERIC:
						cellValue = new CellValue(cell.getNumericCellValue());
						break;
					case STRING:
						cellValue = new CellValue(cell.getStringCellValue());
                        break;
                    case ERROR:
                        if(cell instanceof XSSFCell) {
                            cellValue = new CellValue(((XSSFCell) cell).getErrorCellString());
                        } else {
                            cellValue = new CellValue(cell.getErrorCellValue());
                        }
                        // System.err.format("  Cell[%d,%d] has error: %s\n", row.getRowNum(), column, cellValue.formatAsString());
						break;
					default:
						System.err.format("  Cell[%d,%d] unknown cached formula type %s\n", row.getRowNum(), column, cell.getCachedFormulaResultType());
				}
			}
		}
		return cellValue;
	}
}

