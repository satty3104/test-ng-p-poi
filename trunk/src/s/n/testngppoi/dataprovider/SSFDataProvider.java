package s.n.testngppoi.dataprovider;

import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.Reporter;

import s.n.testngppoi.dataprovider.delegate.SSFDataProviderRowDelegate;
import s.n.testngppoi.exception.TestNgpPoiException;

public class SSFDataProvider implements Iterator<Object[]> {

	private SSFDataProviderRowDelegate delegate;

	private Sheet sheet;

	private int headerRowNum;

	private boolean lastProcessed;

	public SSFDataProvider(Sheet sheet, int headerRowNum) {
		if (sheet == null) {
			// TODO
			throw new TestNgpPoiException("");
		}
		if (headerRowNum <= 0) {
			// TODO
			throw new TestNgpPoiException("");
		}
		init(sheet, headerRowNum);
	}

	private void init(Sheet sheet, int headerRowNum) {
		this.sheet = sheet;
		this.headerRowNum = headerRowNum - 1;
		delegate = createDelegatee(getHeader());
	}

	private Row getHeader() {
		Row header = getRow(headerRowNum);
		if (header == null) {
			// ヘッダ行がない場合は失敗にする
			throw new TestNgpPoiException(
					"There is no header row in the sheet ["
							+ sheet.getSheetName() + "].");
		}
		return header;
	}

	private SSFDataProviderRowDelegate createDelegatee(Row header) {
		return new SSFDataProviderRowDelegate(header, headerRowNum);
	}

	@Override
	public boolean hasNext() {
		int rowNum = getRowNum();
		if (getRow(rowNum) != null) {
			return true;
		}
		processLast(rowNum);
		return false;
	}

	private void processLast(int rowNum) {
		if (lastProcessed) {
			return;
		}
		lastProcessed = _processLast(rowNum);
	}

	private boolean _processLast(int rowNum) {
		if (rowNum == headerRowNum + 1) {
			// ヘッダ行しかなかった場合は成功にするがログには出しておく
			Reporter.log("There is no test in the sheet ["
					+ sheet.getSheetName() + "].");
		}
		if (rowNum != sheet.getLastRowNum() + 1) {
			// 行数がおかしい（空行があるなどの）場合は成功にするがログには出しておく
			Reporter.log("The number of tests run is not the same as the number of rows in the sheet ["
					+ sheet.getSheetName() + "].");
		}
		return true;
	}

	@Override
	public Object[] next() {
		Object[] ret = processRow(getRow(getRowNum()));
		// rowNumを加算するのは戻り値を返す直前にしないと、ログの数字がおかしくなる
		addRowNum();
		return ret;
	}

	private Object[] processRow(Row row) {
		return new Object[] { getMap(row) };
	}

	private Row getRow(int idx) {
		return sheet.getRow(idx);
	}

	private int getRowNum() {
		return delegate.getRowNum();
	}

	private void addRowNum() {
		delegate.addRowNum();
	}

	private Map<String, Object> getMap(Row row) {
		return delegate.getMap(row);
	}

	@Override
	public void remove() {
	}
}
