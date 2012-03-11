package s.n.testngppoi.factory;

import java.util.Iterator;

/**
 * Spread Sheet Format (SSF) 形式のファイルからテストデータを作成するクラス。
 * 
 * @author s_nagai
 * @since 2012/01/23
 */
public interface SSFDataProviderFactory extends DataProviderFactory {

	/**
	 * 引数に渡されたシート名を使ってテストデータを作成します。
	 * 
	 * @param sheetName
	 *            シート名
	 * @return テストデータ
	 */
	public Iterator<Object[]> create(String sheetName);

	/**
	 * 第1引数に渡されたシート名を使ってテストデータを作成します。<br>
	 * このとき、第2引数に渡された行番号をヘッダ行の行番号に設定します。
	 * 
	 * @param sheetName
	 *            シート名
	 * @param headerRowNum
	 *            ヘッダ行の行番号
	 * @return テストデータ
	 */
	public Iterator<Object[]> create(String sheetName, int headerRowNum);
}
