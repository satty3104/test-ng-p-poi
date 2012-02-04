package s.n.testngppoi.dataprovider;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.Reporter;

import s.n.testngppoi.dataprovider.delegate.SSFDataProviderRowDelegate;
import s.n.testngppoi.exception.TestNgpPoiException;

public class SSFDataProvider implements Iterator<Object[]> {

	private SSFDataProviderRowDelegate delegate;

	private Sheet sheet;

	public SSFDataProvider(Sheet sheet) {
		if (sheet == null) {
			throw new NullPointerException("");
		}
		this.sheet = sheet;
		Row header = sheet.getRow(0);
		if (header == null) {
			// ヘッダ行がない場合は失敗にする
			throw new TestNgpPoiException(
					"There is no header row in the sheet ["
							+ sheet.getSheetName() + "].");
		}
		delegate = new SSFDataProviderRowDelegate(header);
	}

	@Override
	public boolean hasNext() {
		int rowNum = delegate.getRowNum();
		if (sheet.getRow(rowNum) != null) {
			return true;
		}
		if (rowNum == 1) {
			// ヘッダ行しかなかった場合は成功にするがログには出しておく
			Reporter.log("There is no test in the sheet ["
					+ sheet.getSheetName() + "].");
		}
		if (rowNum != sheet.getLastRowNum() + 1) {
			// 行数がおかしい（空行があるなどの）場合は成功にするがログには出しておく
			Reporter.log("The number of tests run is not the same as the number of rows in the sheet ["
					+ sheet.getSheetName() + "].");
		}
		return false;
	}

	@Override
	public Object[] next() {
		Object[] ret = processRow(sheet.getRow(delegate.getRowNum()));
		// rowNumを加算するのは戻り値を返す直前にしないと、ログの数字がおかしくなる
		delegate.addRowNum();
		return ret;
	}

	private Object[] processRow(Row row) {
		return new Object[] { delegate.getMap(row) };
	}

	@Override
	public void remove() {
	}
}
