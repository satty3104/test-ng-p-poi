package s.n.testngppoi.dataprovider.delegate.impl;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import s.n.testngppoi.dataprovider.delegate.CellDelegate;
import s.n.testngppoi.dataprovider.delegate.RowDelegate;
import s.n.testngppoi.exception.TestNgpPoiException;
import s.n.testngppoi.service.LogService;
import s.n.testngppoi.util.LogUtil;

/**
 * Spread Sheet Format (SSF) 形式のファイルのセルに関する処理を行うクラス。
 * 
 * @author s_nagai
 * @since 2012/02/04
 */
public class SSFDataProviderRowDelegate implements RowDelegate {

	/** ログ出力を行うクラスのインスタンス */
	private static final LogService log = LogUtil.getLogger();

	private int rowNum;

	private Row header;

	private int maxColumn;

	private CellDelegate delegate;

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

	@Override
	public void addRowNum() {
		rowNum++;
	}

	@Override
	public int getRowNum() {
		return rowNum;
	}

	@Override
	public Map<String, Object> getMap(Row row) {
		// TODO NULLチェック
		if (row.getLastCellNum() > maxColumn) {
			// カラムが多すぎる場合は失敗にしないがログには出しておく
			log.log("There are too many columns in test No." + getRowNum()
					+ ".");
		}
		delegate = new SSFDataProviderCellDelegate(this);
		try {
			for (int i = 0; i < maxColumn; i++) {
				delegate.processCell(header.getCell(i), row.getCell(i));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return delegate.getMap();
	}
}
