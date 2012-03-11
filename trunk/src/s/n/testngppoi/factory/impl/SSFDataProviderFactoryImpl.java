package s.n.testngppoi.factory.impl;

import java.io.File;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import s.n.testngppoi.dataprovider.SSFDataProvider;
import s.n.testngppoi.exception.TestNgpPoiException;
import s.n.testngppoi.factory.SSFDataProviderFactory;
import s.n.testngppoi.reader.WorkbookReader;
import s.n.testngppoi.util.FileUtil;

/**
 * Spread Sheet Format (SSF) 形式のファイルからテストデータを作成するクラス。
 * 
 * @author s_nagai
 * @since 2012/01/30
 */
public class SSFDataProviderFactoryImpl implements SSFDataProviderFactory {

	/** ワークブック */
	private Workbook wb;

	/**
	 * 引数に指定されたファイルを使用して{@code SSFDataProviderFactoryImpl}のインスタンスを生成します。
	 * 
	 * @param file
	 *            ファイル
	 */
	public SSFDataProviderFactoryImpl(final File file) {
		if (file == null) {
			throw new TestNgpPoiException(
					"The argument [file] must not be null.");
		}
		if (FileUtil.isInvalidFile(file)) {
			throw new TestNgpPoiException("The file [" + file.getAbsolutePath()
					+ "] is invalid.");
		}
		wb = new WorkbookReader().read(file);
	}

	/**
	 * {@inheritDoc}<br>
	 * このメソッドを呼び出したメソッド名をシート名に使用します。<br>
	 * ヘッダ行は1行目となります。
	 * 
	 * @see s.n.testngppoi.factory.DataProviderFactory#create()
	 */
	@Override
	public Iterator<Object[]> create() {
		return create(getSheetName(new Throwable()));
	}

	/**
	 * {@inheritDoc}<br>
	 * ヘッダ行は1行目となります。
	 * 
	 * @see s.n.testngppoi.factory.SSFDataProviderFactory#create(java.lang.String)
	 */
	@Override
	public Iterator<Object[]> create(final String sheetName) {
		return create(sheetName, 1);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see s.n.testngppoi.factory.SSFDataProviderFactory#create(java.lang.String,
	 *      int)
	 */
	@Override
	public Iterator<Object[]> create(final String sheetName,
			final int headerRowNum) {
		if (sheetName == null) {
			throw new TestNgpPoiException(
					"The argument [sheetName] must not be null.");
		}
		if (headerRowNum <= 0) {
			throw new TestNgpPoiException(
					"The argument [headerRowNum] must not be lower than 0.");
		}
		return _create(sheetName, headerRowNum);
	}

	/**
	 * 第1引数に渡されたシート名を使ってテストデータを作成します。<br>
	 * このとき、第2引数に渡された行番号をヘッダ行の行番号に設定します。
	 * 
	 * @param sheetName
	 *            シート名
	 * @param headerRowNum
	 *            ヘッダ行の行番号
	 * @return
	 */
	private SSFDataProvider _create(final String sheetName,
			final int headerRowNum) {
		return new SSFDataProvider(getSheet(sheetName), headerRowNum);
	}

	/**
	 * 呼び出し元のメソッド名を取得し、シート名として返します。
	 * 
	 * @param t
	 *            呼び出しの階層構造が含まれているスロー可能オブジェクト
	 * @return シート名
	 */
	private String getSheetName(final Throwable t) {
		return t.getStackTrace()[1].getMethodName();
	}

	/**
	 * 引数に渡されたシート名のシートをワークブックから取得します。
	 * 
	 * @param sheetName
	 *            シート名
	 * @return シート
	 */
	private Sheet getSheet(final String sheetName) {
		final Sheet sheet = wb.getSheet(sheetName);
		if (sheet == null) {
			throw new TestNgpPoiException("The sheet [" + sheetName
					+ "] cannot be found.");
		}
		return sheet;
	}
}
