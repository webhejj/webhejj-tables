/*
 *  Copyright Gergely Nagy <greg@webhejj.hu>
 *
 *  Licensed under the Apache License, Version 2.0;
 *  you may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package hu.webhejj.commons.io.table.excel;

import hu.webhejj.commons.io.table.TableAppender;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * TableAppender for Excel format
 *
 * @author Gergely Nagy <greg@webhejj.hu>
 *
 */
public class ExcelTableAppender implements TableAppender {

	private int rowIndex;
	private int columnIndex;
	private File excelFile;
	private final Sheet sheet;
	private final CellStyle textCellStyle;

	public ExcelTableAppender(File excelFile) {
		this(ExcelUtils.ensureSheet(ExcelUtils.openOrCreateWorkbook(excelFile), 0));
		this.excelFile = excelFile;
	}

	public ExcelTableAppender(Sheet sheet) {
		this.excelFile = null;
		this.sheet = sheet;
		rowIndex = 0; // sheet.getPhysicalNumberOfRows();
		columnIndex = 0;

		textCellStyle = sheet.getWorkbook().createCellStyle();
		textCellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("text"));
	}

	@Override
	public void append(String value) {
		org.apache.poi.ss.usermodel.Row row = sheet.getRow(rowIndex);
		if(row == null) {
			row = sheet.createRow(rowIndex);
		}
		Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cell.setCellValue(value);
		cell.setCellStyle(textCellStyle);
		columnIndex++;
	}

	public void append(int value) {
		append(Integer.toString(value));
	}
	public void append(long value) {
		append(Long.toString(value));
	}

	@Override
	public void newRow() {
		rowIndex++;
		columnIndex = 0;
	}

	@Override
	public void close() throws IOException {
		if(excelFile != null) {
			try (FileOutputStream fos = new FileOutputStream(excelFile)) {
				sheet.getWorkbook().write(fos);
			}
		}
		sheet.getWorkbook().close();
	}
}
