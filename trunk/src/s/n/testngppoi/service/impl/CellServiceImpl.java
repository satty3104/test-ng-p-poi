package s.n.testngppoi.service.impl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

import s.n.testngppoi.exception.TestNgpPoiException;
import s.n.testngppoi.service.CellService;

public class CellServiceImpl implements CellService {

	@Override
	public Object getCellValue(Cell cell) {
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
			throw new TestNgpPoiException();
			// Reporter.log("There are invalid cell type in test No."
			// + delegater.getRowNum() + ", so it replaced to null. "
			// + "Cell type must be string, numeric, date or boolean.");
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
		// TODO 他に特殊文字は？
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
