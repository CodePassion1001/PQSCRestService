package com.proquest.ebd.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class ExcelFileProcessor {

	/**
	 * This Method reads Excel files through XSSF, parses it and returns flat
	 * value in an Array List.
	 * 
	 * @param contents
	 * @return ArrayList<List<String>>
	 * @throws IOException
	 */

	public static ArrayList<HashMap<String,Object>> readExcel(InputStream contents) throws IOException {
		ArrayList<HashMap<String,Object>> sheetData = new ArrayList<HashMap<String,Object>>();
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(contents);
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			int cellPerRow = sheet.getRow(0).getLastCellNum();
			System.out.println("Total Column :: " + cellPerRow);

			ArrayList<String> key = new ArrayList<String>();
			for (Row row : sheet) {
				HashMap<String,Object> rows = new HashMap<String,Object>();
				for (int cn = 0; cn < cellPerRow; cn++) {
					final int rowIndex = row.getRowNum();
					Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);
					if (rowIndex == 0) {
						key.add(cn,getCellValue(cell));
					} else if (cn == 5) {
						DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
						Date date = cell.getDateCellValue();
						String reportDate = df.format(date);
						rows.put(key.get(cn), reportDate);
					} else {
						String str = getCellValue(cell);
						rows.put(key.get(cn), str);
					}
				}
				if(!rows.isEmpty())
				sheetData.add(rows);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (Error er) {
			er.printStackTrace();
			System.out.println(er.getMessage());
		}
		System.out.println("Test:: " + sheetData.size());
		return sheetData;
	}

	protected static String getCellValue(Cell cell) {

		String result = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN:
				result = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				result = String.valueOf(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				result = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BLANK:
				break;
			case Cell.CELL_TYPE_ERROR:
				break;
			}
		}
		return result;
	}
}
