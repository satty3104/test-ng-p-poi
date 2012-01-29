package s.n.testngppoi.dataprovider.delegate;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import s.n.testngppoi.exception.TestNgpPoiException;

public class SSFDataProviderDelegate {

	private int rowNum;

	private Row header;

	private int maxColumn;

	public SSFDataProviderDelegate(Sheet sheet) {
		header = sheet.getRow(getRowNum());
		if (header == null) {
			// ヘッダ行がない場合は失敗にする
			throw new TestNgpPoiException(
					"There is no header row in the sheet ["
							+ sheet.getSheetName() + "].");
		}
		maxColumn = header.getLastCellNum();
		addRowNum();
	}

	public void addRowNum() {
		rowNum++;
	}

	public int getRowNum() {
		return rowNum;
	}

	public Map<String, Object> getMap(Row row) {
		if (row.getLastCellNum() > maxColumn) {
			// TODO カラムが多すぎる場合は失敗にしないがログには出しておく
			System.out.println("There are too many columns in test No."
					+ row.getRowNum() + ".");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < maxColumn; i++) {
			String key = processHeaderCell(header.getCell(i));
			processCell(map, key, row.getCell(i));
		}
		return map;
	}

	private String processHeaderCell(Cell headerCell) {
		if (getRowNum() == 1) {
			// 初回はヘッダのチェックもする
			checkHeaderCell(headerCell);
		}
		return headerCell.getRichStringCellValue().getString();
	}

	private void checkHeaderCell(Cell headerCell) {
		if (headerCell == null) {
			// ヘッダが未入力の場合は失敗
			throw new TestNgpPoiException(
					"The cell with no value is found in the header row.");
		}
		if (headerCell.getCellType() != Cell.CELL_TYPE_STRING) {
			// ヘッダのデータ型が文字列でない場合は失敗
			throw new TestNgpPoiException(
					"Header row's cell must be String type.");
		}
	}

	private void processCell(Map<String, Object> map, String key, Cell cell) {
		if (map.containsKey(key)) {
			// TODO キーが重複していたらログには出しておく
			System.out.println("Key in header cell is duplicated. [" + key
					+ "]");
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
			System.out.println("There are invalid cell type in test No."
					+ getRowNum() + ", so it replaced to null. "
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
}
