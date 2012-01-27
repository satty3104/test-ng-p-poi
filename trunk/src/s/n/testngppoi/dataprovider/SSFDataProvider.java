package s.n.testngppoi.dataprovider;

import static org.testng.Assert.fail;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import s.n.testngppoi.dataprovider.delegate.SSFDataProviderDelegate;

public class SSFDataProvider implements Iterator<Object[]> {

	private Sheet sheet;

	private Row header;

	private int rowNum;

	private SSFDataProviderDelegate delegate;

	public SSFDataProvider(Sheet sheet) {
		if (sheet == null) {
			throw new NullPointerException("");
		}
		delegate = new SSFDataProviderDelegate();
		this.sheet = sheet;
		init();
	}

	private void init() {
		header = sheet.getRow(rowNum);
		if (header == null) {
			// ヘッダ行がない場合は失敗にする
			fail("There is no header row in the sheet [" + sheet.getSheetName() + "].");
		}

		rowNum++;
		if (sheet.getRow(rowNum) == null) {
			// ヘッダ行しかない場合は成功にするがログには出しておく
			System.out.println("There is no test in the sheet [" + sheet.getSheetName() + "].");
		}
	}

	@Override
	public boolean hasNext() {
		Row row = sheet.getRow(rowNum);
		if (row != null) {
			return true;
		}
		if (rowNum != sheet.getLastRowNum() + 1) {
			// 行数がおかしい（空行があるなどの）場合は成功にするがログには出しておく
			System.out.println("The number of tests run is not the same as the number of rows in the sheet ["
					+ sheet.getSheetName() + "].");
		}
		return false;
	}

	@Override
	public Object[] next() {
		Object[] ret = processRow(sheet.getRow(rowNum));

		// rowNumを加算するのは戻り値を返す直前にしないと、ログの数字がおかしくなる
		rowNum++;
		return ret;
	}

	private Object[] processRow(Row row) {
		return new Object[] { delegate.getMap(row, header, rowNum) };
	}

	@Override
	public void remove() {
	}
}
