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

/**
 * Spread Sheet Format (SSF) 形式のファイルから1行ごとにテストデータを作成して返却するクラス。
 * 
 * @author s_nagai
 * @since 2012/01/22
 */
public class SSFDataProvider implements Iterator<Object[]> {

	/** ログ出力を行うクラスのインスタンス */
	private static final LogService log = LogUtil.getLogger();

	/** 行に対する処理を行なうクラスのインスタンス */
	private RowDelegate delegate;

	/** 処理対象シート */
	private Sheet sheet;

	/** ヘッダ行番号 */
	private int headerRowNum;

	/** 1回でも終了処理が行なわれたか否か */
	private boolean lastProcessed;

	/**
	 * 引数に指定されたシートとヘッダ行番号を用いて、新しい{@code SSFDataProvider}のインスタンスを作成します。
	 * 
	 * @param sheet
	 *            シート
	 * @param headerRowNum
	 *            ヘッダ行番号
	 */
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

	/**
	 * 引数に指定されたシートとヘッダ行番号を用いて、{@code SSFDataProvider}のインスタンスを初期化します。
	 * 
	 * @param sheet
	 *            シート
	 * @param headerRowNum
	 *            ヘッダ行番号
	 */
	private void init(final Sheet sheet, final int headerRowNum) {
		this.sheet = sheet;
		// 行は0始まりなので1引いておく
		this.headerRowNum = headerRowNum - 1;
		delegate = createDelegate(getHeader());
	}

	/**
	 * 処理対象シートからヘッダ行オブジェクトを取得して返します。<br>
	 * ヘッダ行オブジェクトが取得できなかった場合は{@link TestNgpPoiException}がスローされます。
	 * 
	 * @return ヘッダ行オブジェクト
	 */
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

	/**
	 * ヘッダ行をもとに、行に対する処理を行なうクラスのインスタンスを生成します。
	 * 
	 * @param header
	 *            ヘッダ行オブジェクト
	 * @return 行に対する処理を行なうクラスのインスタンス
	 */
	private RowDelegate createDelegate(final Row header) {
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
