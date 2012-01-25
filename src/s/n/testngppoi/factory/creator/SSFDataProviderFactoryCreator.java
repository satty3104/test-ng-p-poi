package s.n.testngppoi.factory.creator;

import s.n.testngppoi.factory.SSFDataProviderFactory;
import s.n.testngppoi.factory.impl.HSSFDataProviderFactory;
import s.n.testngppoi.factory.impl.XSSFDataProviderFactory;
import s.n.testngppoi.type.Type;

/**
 * Spread Sheet Format (SSF) 形式のファイルからテストデータを作成するクラスの
 * インスタンスを生成するクラス。
 * 
 * @author s_nagai
 * @since 2012/01/23
 */
public class SSFDataProviderFactoryCreator {

	/**
	 * 新しい{@code SSFDataProviderFactoryCreator}クラスのインスタンスを
	 * 生成します。
	 */
	private SSFDataProviderFactoryCreator() {
	}

	/**
	 * 引数に指定されたファイルタイプを元に、テストデータを作成するクラスの
	 * インスタンスを作成して返します。
	 * 
	 * @param type SSF 形式ファイルのファイルタイプの定義
	 * @return テストデータを作成するクラスのインスタンス
	 * @throws NullPointerException
	 * 			引数の{@code type}が{@code null}であった場合
	 */
	public static SSFDataProviderFactory getInstance(final Type type)
			throws NullPointerException {
		if (type == null) {
			throw new NullPointerException(
					"The argument \"type\" must not be null.");
		}
		switch (type) {
		case HSSF:
			return new HSSFDataProviderFactory();
		case XSSF:
			return new XSSFDataProviderFactory();
		default:
			throw new IllegalArgumentException(
					"The argument \"type\" is illegal. [" + type + "]");
		}
	}
}
