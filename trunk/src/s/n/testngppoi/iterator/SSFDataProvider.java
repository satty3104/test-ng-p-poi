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
		init();
	}

	private void init() {
		header = sheet.getRow(rowNum);
		if (header == null) {
			// ヘッダ行がない場合は失敗にする
			fail("There is no header row in the sheet [" + sheet.getSheetName() + "].");
		}
		maxColumn = header.getLastCellNum();

		rowNum++;
		if (sheet.getRow(rowNum) == null) {
			// ヘッダ行しかない場合は成功にするがログには出しておく
			System.out.println("There is no test in the sheet [" + sheet.getSheetName() + "].");
		}
	}

	@Override
	public boolean hasNext() {
		Row row = sheet.getRow(rowNum);
		if (row == null) {
			if (rowNum != sheet.getLastRowNum() + 1) {
				// 行数がおかしい（空行があるなどの）場合は成功にするがログには出しておく
				System.out.println("The number of tests run is not the same as the number of rows in the sheet ["
						+ sheet.getSheetName() + "].");
				System.out.println(rowNum + " " + sheet.getLastRowNum());
			}
			return false;
		}
		return true;
	}

	@Override
	public Object[] next() {
		Object[] ret = processRow(sheet.getRow(rowNum));

		// rowNumを加算するのは戻り値を返す直前にしないと、ログの数字がおかしくなる
		rowNum++;
		return ret;
	}

	private Object[] processRow(Row row) {
		if (row.getLastCellNum() > maxColumn) {
			// カラムが多すぎる場合は失敗にしないがログには出しておく
			System.out.println("There are too many columns in test No." + row.getRowNum() + ".");
		}
		return new Object[] { getMap(row) };
	}

	private Map<String, Object> getMap(Row row) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < maxColumn; i++) {
			String key = processHeaderCell(header.getCell(i));
			processCell(map, key, row.getCell(i));
		}
		return map;
	}

	private String processHeaderCell(Cell headerCell) {
		if (rowNum == 1) {
			// 初回はヘッダのチェックもする
			checkHeaderCell(headerCell);
		}
		return headerCell.getRichStringCellValue().getString();
	}

	private void checkHeaderCell(Cell headerCell) {
		if (headerCell == null) {
			// ヘッダが未入力の場合は失敗
			fail("The cell with no value is found in the header row.");
		}
		if (headerCell.getCellType() != Cell.CELL_TYPE_STRING) {
			// ヘッダのデータ型が文字列でない場合は失敗
			fail("Header row's cell must be String type.");
		}
	}

	private void processCell(Map<String, Object> map, String key, Cell cell) {
		if (map.containsKey(key)) {
			// キーが重複していたらログには出しておく
			System.out.println("Key in header cell is duplicated. [" + key + "]");
		}
		map.put(key, getValue(cell));
	}

	protected Object getValue(Cell cell) {
		if (cell == null) {
			return processNull();
		}
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				return processBlank(cell);
			case Cell.CELL_TYPE_STRING:
				return processString(cell);
			case Cell.CELL_TYPE_NUMERIC:
				return processNumeric(cell);
			case Cell.CELL_TYPE_BOOLEAN:
				return processBoolean(cell);
			default:
				System.out.println("There are invalid cell type in test No." + rowNum + ", so it replaced to null. "
						+ "Cell type must be string, numeric, date or boolean.");
				return null;
		}
	}

	protected Object processNull() {
		return null;
	}

	protected Object processBlank(Cell cell) {
		return null;
	}

	protected Object processString(Cell cell) {
		String cellValue = cell.getRichStringCellValue().getString();
		if ("null".equals(cellValue)) {
			return null;
		}
		if ("\"\"".equals(cellValue)) {
			return "";
		}
		return cell.getRichStringCellValue().getString();
	}

	protected Object processNumeric(Cell cell) {
		if (DateUtil.isCellDateFormatted(cell)) {
			return cell.getDateCellValue();
		}
		return Double.valueOf(cell.getNumericCellValue());
	}

	protected Object processBoolean(Cell cell) {
		return Boolean.valueOf(cell.getBooleanCellValue());
	}

	@Override
	public void remove() {
	}
}
