package s.n.testngppoi.service;

/**
 * ログ出力を行うクラスのインタフェース。
 * 
 * @author s_nagai
 * @since 2012/03/09
 */
public interface LogService {

	/**
	 * 引数に渡されたメッセージをログ出力します。
	 * 
	 * @param message
	 *            メッセージ
	 */
	public void log(String message);
}
