package s.n.testngppoi.factory;

import java.util.Iterator;

/**
 * テストデータを作成するクラスのインタフェース。
 * 
 * @author s_nagai
 * @since 2012/03/09
 */
public interface DataProviderFactory {

	/**
	 * デフォルトの設定を使ってテストデータを作成します。
	 * 
	 * @return テストデータ
	 */
	public Iterator<Object[]> create();
}
