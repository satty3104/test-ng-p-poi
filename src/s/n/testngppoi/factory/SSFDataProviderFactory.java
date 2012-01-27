package s.n.testngppoi.factory;

import java.util.Iterator;

/**
 * Spread Sheet Format (SSF) 形式のファイルからテストデータを作成するクラス。
 * 
 * @author s_nagai
 * @since 2012/01/23
 */
public interface SSFDataProviderFactory {

	/**
	 * このメソッドを呼びだしたクラスが存在するディレクトリから
	 * Spread Sheet Format (SSF) 形式のファイルを取得し、
	 * テストデータを作成します。<br>
	 * テストデータはこのメソッドを呼び出したクラスの名称を持つファイルの中で、
	 * このメソッドを呼び出したメソッドの名称をもつシートから
	 * 取得します。
	 * 
	 * @return テストデータ
	 */
	public Iterator<Object[]> create();

	/**
	 * このメソッドを呼びだしたクラスが存在するディレクトリから
	 * Spread Sheet Format (SSF) 形式のファイルを取得し、
	 * テストデータを作成します。<br>
	 * テストデータは第一引数に指定されたファイル名のファイルの中で、
	 * 第二引数に指定されたシート名のシートから取得します。
	 * 
	 * @param fileName テストデータを記述したファイル名
	 * @param sheetName テストデータを記述したシート名
	 * @return テストデータ
	 */
	public Iterator<Object[]> create(String fileName, String sheetName);

	/**
	 * 第一引数に指定されたディレクトリから Spread Sheet Format (SSF) 形式の
	 * ファイルを取得し、テストデータを作成します。<br>
	 * テストデータは第二引数に指定されたファイル名のファイルの中で、
	 * 第三引数に指定されたシート名のシートから取得します。
	 * 
	 * @param filePath
	 * 			テストデータを記述したファイルが存在するディレクトリの絶対パス
	 * @param fileName テストデータを記述したファイル名
	 * @param sheetName テストデータを記述したシート名
	 * @return テストデータ
	 */
	public Iterator<Object[]> create(String filePath, String fileName,
			String sheetName);

	public Iterator<Object[]> changeSheet(String sheetName);
}
