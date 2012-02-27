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

	private Type type;

	private Class<?> invoker;

	public SSFDataProviderFactoryCreator() {
		init(Type.XSSF);
	}

	public SSFDataProviderFactoryCreator(Type type) {
		if (type == null) {
			throw new TestNgpPoiException(
					"The argument [type] must not be null.");
		}
		init(type);
	}

	private void init(Type type) {
		this.type = type;
	}

	public SSFDataProviderFactory getFactory() {
		setInvoker(new Throwable());
		return _getFactory(getFilePath(getFileName()));
	}

	public SSFDataProviderFactory getFactory(String fileName) {
		if (fileName == null) {
			throw new TestNgpPoiException(
					"The argument [fileName] must not be null.");
		}
		setInvoker(new Throwable());
		return _getFactory(getFilePath(fileName));
	}

	public SSFDataProviderFactory getFactory(String dirPath, String fileName) {
		if (dirPath == null) {
			throw new TestNgpPoiException(
					"The argument [dirPath] must not be null.");
		}
		if (fileName == null) {
			throw new TestNgpPoiException(
					"The argument [fileName] must not be null.");
		}
		return _getFactory(getFilePath(dirPath, fileName));
	}

	private SSFDataProviderFactory _getFactory(String filePath) {
		return new SSFDataProviderFactoryImpl(getFile(filePath));
	}

	private String getFileName() {
		return invoker.getSimpleName() + type.getExtension();
	}

	private void setInvoker(Throwable t) {
		StackTraceElement elem = t.getStackTrace()[1];
		try {
			invoker = Class.forName(elem.getClassName());
		} catch (ClassNotFoundException e) {
			throw new TestNgpPoiException("The caller class ["
					+ elem.getClassName() + "] cannot be found.", e);
		}
	}

	private String getFilePath(String fileName) {
		// 呼び出し元クラスと同階層にあるテストデータファイルのURLを取得
		URL fileUrl = invoker.getResource(fileName);
		if (fileUrl == null) {
			throw new TestNgpPoiException("The file ["
					+ new File(invoker.getResource(
							invoker.getSimpleName() + ".class").getPath())
							.getParentFile().getAbsolutePath() + File.separator
					+ fileName + "] is not exists.");
		}
		return fileUrl.getPath();
	}

	private String getFilePath(String dirPath, String fileName) {
		return dirPath + File.separator + fileName;
	}

	private File getFile(String filePath) {
		File f = new File(filePath);
		if (FileUtil.isInvalidFile(f)) {
			throw new TestNgpPoiException("The file [" + f.getAbsolutePath()
					+ "] is not a file or can not read.");
		}
		return f;
	}
}
