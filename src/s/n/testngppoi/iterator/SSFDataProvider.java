package s.n.testngppoi.iterator;

import static org.testng.Assert.fail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class SSFDataProvider implements Iterator<Object[]> {

	private Sheet sheet;

	private Row header;

	private int rowNum;

	private int maxColumn;

	public SSFDataProvider(Sheet sheet) {
		if (sheet == null) {
			throw new NullPointerException("");
		}
		this.sheet = sheet;

		header = sheet.getRow(rowNum);
		if (header == null) {
			// ヘッダ行がない場合は失敗にする
			fail("There is no header row in the sheet [" + sheet.getSheetName()
					+ "].");
		}
		maxColumn = header.getLastCellNum();

		rowNum++;
		if (sheet.getRow(rowNum) == null) {
			// ヘッダ行しかない場合は成功にするがログには出しておく
			System.out.println("There is no test in the sheet ["
					+ sheet.getSheetName() + "].");
		}
	}

	@Override
	public boolean hasNext() {
		return sheet.getRow(rowNum) != null;
	}

	@Override
	public Object[] next() {
		Row row = sheet.getRow(rowNum);
		if (row.getLastCellNum() > maxColumn) {
			// カラムが多すぎる場合は失敗にしないがログには出しておく
			System.out.println("There are too many columns in test No."
					+ row.getRowNum() + ".");
		}

		// rowNumを加算するのは戻り値を返す直前にしないと、ログの数字がおかしくなる
		Object[] ret = new Object[] { getMap(row) };
		rowNum++;
		return ret;
	}

	private Map<String, Object> getMap(Row row) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < maxColumn; i++) {
			Cell headerCell = header.getCell(i);
			if (headerCell == null) {
				// ヘッダが未入力の場合は失敗
				fail("The cell with no value is found in the header row.");
			}
			if (headerCell.getCellType() != Cell.CELL_TYPE_STRING) {
				// ヘッダのデータ型が文字列でない場合は失敗
				fail("Header row's cell must be String type.");
			}
			String key = headerCell.getRichStringCellValue().getString();
			processCell(row, map, key, row.getCell(i));
		}
		return map;
	}

	private void processCell(Row row, Map<String, Object> map, String key,
			Cell dataCell) {
		if (dataCell == null) {
			map.put(key, null);
			return;
		}
		switch (dataCell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			map.put(key, null);
			break;
		case Cell.CELL_TYPE_STRING:
			String cellValue = dataCell.getRichStringCellValue().getString();
			if ("null".equals(cellValue)) {
				map.put(key, null);
				break;
			}
			if ("\"\"".equals(cellValue)) {
				map.put(key, "");
				break;
			}
			map.put(key, dataCell.getRichStringCellValue().getString());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(dataCell)) {
				map.put(key, dataCell.getDateCellValue());
			} else {
				map.put(key, dataCell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			map.put(key, dataCell.getBooleanCellValue());
			break;
		default:
			System.out.println("There are invalid cell type in test No."
					+ rowNum + ", so it replaced to null. "
					+ "Cell type must be string, numeric, date, boolean.");
			map.put(key, null);
		}
	}

	@Override
	public void remove() {
	}
}
