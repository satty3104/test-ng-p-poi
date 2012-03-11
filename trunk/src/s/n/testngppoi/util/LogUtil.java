package s.n.testngppoi.util;

import s.n.testngppoi.service.LogService;
import s.n.testngppoi.service.impl.LogServiceImpl;

/**
 * ログ出力を行うクラスを生成するクラス。
 * 
 * @author s_nagai
 * @since 2012/01/24
 */
public class LogUtil {

	/** ログ出力を行うクラスのインスタンス */
	private final static LogService logService = new LogServiceImpl();

	/**
	 * 新しい{@code LogUtil}のインスタンスを生成します。<br>
	 * 外部からインスタンス化できないように{@code private}としています。
	 */
	private LogUtil() {
	}

	/**
	 * ログ出力を行うクラスのインスタンスを返します。
	 * 
	 * @return ログ出力を行うクラスのインスタンス
	 */
	public static LogService getLogger() {
		return logService;
	}
}
