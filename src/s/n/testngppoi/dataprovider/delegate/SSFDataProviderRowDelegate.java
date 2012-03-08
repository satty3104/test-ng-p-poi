package s.n.testngppoi.dataprovider.delegate;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.testng.Reporter;

import s.n.testngppoi.exception.TestNgpPoiException;

public class SSFDataProviderRowDelegate {

	private int rowNum;

	private Row header;

	private int maxColumn;

	private CellDelegate delegatee;

	public SSFDataProviderRowDelegate(Row header, int headerRowNum) {
		this.header = header;
		rowNum = headerRowNum + 1;
		maxColumn = header.getLastCellNum();
		checkHeaderCell();
	}

	private void checkHeaderCell() {
		for (int i = 0; i < maxColumn; i++) {
			checkHeaderCell(header.getCell(i));
		}
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

	public void addRowNum() {
		rowNum++;
	}

	public int getRowNum() {
		return rowNum;
	}

	public Map<String, Object> getMap(Row row) {
		// TODO NULLチェック
		if (row.getLastCellNum() > maxColumn) {
			// カラムが多すぎる場合は失敗にしないがログには出しておく
			Reporter.log("There are too many columns in test No." + getRowNum()
					+ ".");
		}
		delegatee = new SSFDataProviderCellDelegate(this);
		try {
			for (int i = 0; i < maxColumn; i++) {
				delegatee.processCell(header.getCell(i), row.getCell(i));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return delegatee.getMap();
	}
}
