package s.n.testngppoi.factory;

import java.util.Iterator;

/**
 * Spread Sheet Format (SSF) 形式のファイルからテストデータを作成するクラス。
 * 
 * @author s_nagai
 * @since 2012/01/23
 */
public interface SSFDataProviderFactory {

	public Iterator<Object[]> create();

	public Iterator<Object[]> create(String sheetName);
}
