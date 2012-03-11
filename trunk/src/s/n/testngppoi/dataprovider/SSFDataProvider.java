package s.n.testngppoi.dataprovider;

import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import s.n.testngppoi.dataprovider.delegate.RowDelegate;
import s.n.testngppoi.dataprovider.delegate.impl.SSFDataProviderRowDelegate;
import s.n.testngppoi.exception.TestNgpPoiException;
import s.n.testngppoi.service.LogService;
import s.n.testngppoi.util.LogUtil;

public class SSFDataProvider implements Iterator<Object[]> {

	/** ログ出力を行うクラスのインスタンス */
	private static final LogService log = LogUtil.getLogger();

	private RowDelegate delegate;

	private Sheet sheet;

	private int headerRowNum;

	private boolean lastProcessed;

	public SSFDataProvider(final Sheet sheet, final int headerRowNum) {
		if (sheet == null) {
			throw new TestNgpPoiException(
					"The argument [sheet] must not be null.");
		}
		if (headerRowNum <= 0) {
			throw new TestNgpPoiException(
					"The argument [headerRowNum] must more than 0.");
		}
		init(sheet, headerRowNum);
	}

	private void init(final Sheet sheet, final int headerRowNum) {
		this.sheet = sheet;
		// 行は0始まりなので1引いておく
		this.headerRowNum = headerRowNum - 1;
		delegate = createDelegatee(getHeader());
	}

	private Row getHeader() {
		final Row header = getRow(headerRowNum);
		if (header == null) {
			// ヘッダ行がない場合は失敗にする
			throw new TestNgpPoiException(
					"There is no header row in the sheet ["
							+ sheet.getSheetName() + "].");
		}
		return header;
	}

	private RowDelegate createDelegatee(final Row header) {
		return new SSFDataProviderRowDelegate(header, headerRowNum);
	}

	@Override
	public boolean hasNext() {
		final int rowNum = getRowNum();
		if (getRow(rowNum) != null) {
			return true;
		}
		processLast(rowNum);
		return false;
	}

	private void processLast(final int rowNum) {
		if (lastProcessed) {
			return;
		}
		lastProcessed = _processLast(rowNum);
	}

	private boolean _processLast(final int rowNum) {
		if (rowNum == headerRowNum + 1) {
			// ヘッダ行しかなかった場合は成功にするがログには出しておく
			log.log("There is no test in the sheet [" + sheet.getSheetName()
					+ "].");
		}
		if (rowNum != sheet.getLastRowNum() + 1) {
			// 行数がおかしい（空行があるなどの）場合は成功にするがログには出しておく
			log.log("The number of tests run is not the same as the number of rows in the sheet ["
					+ sheet.getSheetName() + "].");
		}
		return true;
	}

	@Override
	public Object[] next() {
		final Object[] ret = processRow(getRow(getRowNum()));
		// rowNumを加算するのは戻り値を返す直前にしないと、ログの数字がおかしくなる
		addRowNum();
		return ret;
	}

	private Object[] processRow(final Row row) {
		return new Object[] { getMap(row) };
	}

	private Row getRow(final int idx) {
		return sheet.getRow(idx);
	}

	private int getRowNum() {
		return delegate.getRowNum();
	}

	private void addRowNum() {
		delegate.addRowNum();
	}

	private Map<String, Object> getMap(final Row row) {
		return delegate.getMap(row);
	}

	@Override
	public void remove() {
	}
}
