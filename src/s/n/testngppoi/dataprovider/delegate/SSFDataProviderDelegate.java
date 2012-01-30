package s.n.testngppoi.dataprovider.delegate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import s.n.testngppoi.exception.TestNgpPoiException;

public class SSFDataProviderDelegate {

	private int rowNum = 1;

	private Row header;

	private int maxColumn;

	public SSFDataProviderDelegate(Row header) {
		this.header = header;
		maxColumn = header.getLastCellNum();
		checkHeaderCell();
	}

	private void checkHeaderCell() {
		for (int i = 0; i < maxColumn; i++) {
			Cell headerCell = header.getCell(i);
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
			processCell(map, header.getCell(i), row.getCell(i));
		}
		return map;
	}

	private void processCell(Map<String, Object> map, Cell headerCell, Cell cell) {
		String headerValue = headerCell.getRichStringCellValue().getString();
		String[] headerElements = headerValue.split(":", -1);
		if (headerElements.length > 2) {
			throw new TestNgpPoiException("");
		}
		if (headerElements.length == 1) {
			String key = headerElements[0];
			if (map.containsKey(key)) {
				// TODO キーが重複していたらログには出しておく
				System.out.println("Key in header cell is duplicated. [" + key
						+ "]");
			}
			map.put(key, getValue(cell));
			return;
		}

		String className = headerElements[0];
		String valiableName = headerElements[1];

		String[] valiableNameElements = valiableName.split(".", -1);

		Object o = null;
		try {
			o = Class.forName(className).newInstance();
		} catch (ClassNotFoundException e) {
			// TODO
		} catch (InstantiationException e) {
			// TODO
		} catch (IllegalAccessException e) {
			// TODO
		}

		Field f = null;
		try {
			f = o.getClass().getDeclaredField(valiableName);
			f.setAccessible(true);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
		}
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
		return cell.getRichStringCellValue().getString()
				.replaceAll("\\[TAB\\]", "\t");
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
