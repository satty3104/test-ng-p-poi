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
		type = Type.XSSF;
	}

	public SSFDataProviderFactoryCreator(Type type) {
		if (type == null) {
			throw new TestNgpPoiException(
					"The argument [type] must not be null.");
		}
		this.type = type;
	}

	public SSFDataProviderFactory getFactory() {
		invoker = getInvoker(new Throwable());
		String fileName = invoker.getSimpleName() + getExtension();
		return createFactory(getFilePath(fileName));
	}

	public SSFDataProviderFactory getFactory(String fileName) {
		if (fileName == null) {
			throw new TestNgpPoiException(
					"The argument [fileName] must not be null.");
		}
		invoker = getInvoker(new Throwable());
		return createFactory(getFilePath(fileName));
	}

	public SSFDataProviderFactory getFactory(String dirPath, String fileName) {
		return createFactory(dirPath + File.separator + fileName);
	}

	private SSFDataProviderFactory createFactory(String filePath) {
		return new SSFDataProviderFactoryImpl(getFile(filePath));
	}

	private String getExtension() {
		return type.getExtension();
	}

	private Class<?> getInvoker(Throwable t) {
		StackTraceElement elem = t.getStackTrace()[1];
		try {
			return Class.forName(elem.getClassName());
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

	private File getFile(String filePath) {
		File f = new File(filePath);
		if (FileUtil.isInvalid(f)) {
			throw new TestNgpPoiException("The file [" + f.getAbsolutePath()
					+ "] is not a file or can not read.");
		}
		return f;
	}
}
