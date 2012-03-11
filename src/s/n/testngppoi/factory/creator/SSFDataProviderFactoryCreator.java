package s.n.testngppoi.factory.creator;

import java.io.File;
import java.net.URL;

import s.n.testngppoi.exception.TestNgpPoiException;
import s.n.testngppoi.factory.SSFDataProviderFactory;
import s.n.testngppoi.factory.impl.SSFDataProviderFactoryImpl;
import s.n.testngppoi.type.Type;
import s.n.testngppoi.util.FileUtil;

/**
 * Spread Sheet Format (SSF) 形式のファイルからテストデータを作成するクラスのインスタンスを生成するクラス。
 * 
 * @author s_nagai
 * @since 2012/01/23
 */
public class SSFDataProviderFactoryCreator {

	/** ファイル形式 */
	private Type type;

	/** {@link #getFactory()}、{@link #getFactory(String)}の呼び出し元クラス */
	private Class<?> invoker;

	/**
	 * XML Spread Sheet Format 形式を指定して{@code SSFDataProviderFactoryCreator}
	 * のインスタンスを生成します。
	 */
	public SSFDataProviderFactoryCreator() {
		init(Type.XSSF);
	}

	/**
	 * 引数に渡されたファイル形式で{@code SSFDataProviderFactoryCreator}のインスタンスを生成します。
	 * 
	 * @param type
	 *            ファイル形式
	 */
	public SSFDataProviderFactoryCreator(final Type type) {
		if (type == null) {
			throw new TestNgpPoiException(
					"The argument [type] must not be null.");
		}
		init(type);
	}

	/**
	 * 引数に渡されたファイル形式でこのクラスのインスタンスを初期化します。
	 * 
	 * @param type
	 *            ファイル形式
	 */
	private void init(final Type type) {
		this.type = type;
	}

	/**
	 * {@link SSFDataProviderFactory}のインスタンスを生成して返します。<br>
	 * このとき、呼び出し元クラスのファイルが存在するディレクトリ + 呼び出し元クラス名 + ファイル形式 の SSF ファイルを使用します。
	 * 
	 * @return {@link SSFDataProviderFactory}のインスタンス
	 */
	public SSFDataProviderFactory getFactory() {
		setInvoker(new Throwable());
		return _getFactory(getFilePath(getFileName()));
	}

	/**
	 * {@link SSFDataProviderFactory}のインスタンスを生成して返します。<br>
	 * このとき、呼び出し元クラスのファイルが存在するディレクトリ + 引数に渡されたファイル名 の SSF ファイルを使用します。
	 * 
	 * @param fileName
	 *            ファイル名
	 * @return {@link SSFDataProviderFactory}のインスタンス
	 */
	public SSFDataProviderFactory getFactory(final String fileName) {
		if (fileName == null) {
			throw new TestNgpPoiException(
					"The argument [fileName] must not be null.");
		}
		setInvoker(new Throwable());
		return _getFactory(getFilePath(fileName));
	}

	/**
	 * {@link SSFDataProviderFactory}のインスタンスを生成して返します。<br>
	 * このとき、第1引数に渡されたディレクトリ + 第2引数に渡されたファイル名 の SSF ファイルを使用します。
	 * 
	 * @param dirPath
	 *            ディレクトリパス
	 * @param fileName
	 *            ファイル名
	 * @return {@link SSFDataProviderFactory}のインスタンス
	 */
	public SSFDataProviderFactory getFactory(final String dirPath,
			final String fileName) {
		if (dirPath == null) {
			throw new TestNgpPoiException(
					"The argument [dirPath] must not be null.");
		}
		if (fileName == null) {
			throw new TestNgpPoiException(
					"The argument [fileName] must not be null.");
		}
		return _getFactory(FileUtil.getFilePath(dirPath, fileName));
	}

	/**
	 * {@link SSFDataProviderFactory}のインスタンスを生成して返します。<br>
	 * このとき、引数に渡されたファイルパスをもつ SSF ファイルを使用します。
	 * 
	 * @param filePath
	 *            ファイルパス
	 * @return {@link SSFDataProviderFactory}のインスタンス
	 */
	private SSFDataProviderFactory _getFactory(final String filePath) {
		return new SSFDataProviderFactoryImpl(FileUtil.getFile(filePath));
	}

	/**
	 * 呼び出し元クラス名とファイル形式からファイル名を作成します。<br>
	 * ただし、{@code #invoker}と{@code #type}が{@code null}ではないことを前提とします。
	 * 
	 * @return ファイル名
	 */
	private String getFileName() {
		return invoker.getSimpleName() + type.getExtension();
	}

	/**
	 * 呼び出し元クラスを取得し、このクラスのインスタンスに設定します。
	 * 
	 * @param t
	 *            呼び出しの階層構造が含まれているスロー可能オブジェクト
	 */
	private void setInvoker(final Throwable t) {
		final StackTraceElement elem = t.getStackTrace()[1];
		try {
			invoker = Class.forName(elem.getClassName());
		} catch (ClassNotFoundException e) {
			throw new TestNgpPoiException("The caller class ["
					+ elem.getClassName() + "] cannot be found.", e);
		}
	}

	/**
	 * 呼び出し元クラスのクラスファイルが存在するディレクトリを使って、引数で渡されたファイルの絶対パスを返します。
	 * 
	 * @param fileName
	 *            ファイル名
	 * @return ファイルパス
	 */
	private String getFilePath(final String fileName) {
		// 呼び出し元クラスと同階層にあるテストデータファイルのURLを取得
		final URL fileUrl = invoker.getResource(fileName);
		if (fileUrl == null) {
			throw new TestNgpPoiException("The file ["
					+ new File(invoker.getResource(
							invoker.getSimpleName() + ".class").getPath())
							.getParentFile().getAbsolutePath() + File.separator
					+ fileName + "] is not exists.");
		}
		return fileUrl.getPath();
	}
}
