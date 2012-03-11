package s.n.testngppoi.service.impl;

import org.testng.Reporter;

import s.n.testngppoi.service.LogService;

/**
 * ログ出力を行うクラス。
 * 
 * @author s_nagai
 * @since 2012/03/09
 */
public class LogServiceImpl implements LogService {

	/**
	 * {@inheritDoc}<br>
	 * このメソッドでは、{@link Reporter#log(String)}メソッドを利用してログ出力を行います。
	 * 
	 * @see s.n.testngppoi.service.LogService#log(java.lang.String)
	 */
	@Override
	public void log(final String message) {
		Reporter.log(message);
	}
}
