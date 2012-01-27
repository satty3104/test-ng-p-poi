package s.n.testngppoi.factory.impl;

import static org.testng.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import s.n.testngppoi.dataprovider.SSFDataProvider;
import s.n.testngppoi.factory.SSFDataProviderFactory;
import s.n.testngppoi.reader.WorkbookReader;
import s.n.testngppoi.util.FileUtil;

public abstract class AbstractSSFDataProviderFactory implements SSFDataProviderFactory {

	private Class<?> callerClass;

	@Override
	public Iterator<Object[]> create() {
		StackTraceElement caller = new Throwable().getStackTrace()[1];
		callerClass = getCallerClass(caller);
		return create(callerClass.getSimpleName() + getExtension(), caller.getMethodName());
	}

	@Override
	public Iterator<Object[]> create(String fileName, String sheetName) {
		if (callerClass == null) {
			callerClass = getCallerClass(new Throwable().getStackTrace()[1]);
		}
		return create(getDataFile(getFilePath(fileName)), sheetName);
	}

	@Override
	public Iterator<Object[]> create(String filePath, String fileName, String sheetName) {
		return create(getDataFile(filePath + File.separator + fileName), sheetName);
	}

	private Iterator<Object[]> create(File file, String sheetName) {
		return new SSFDataProvider(getSheet(file, sheetName));
	}

	private Class<?> getCallerClass(StackTraceElement elem) {
		Class<?> c = null;
		try {
			c = Class.forName(elem.getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("The caller class [" + elem.getClassName() + "] cannot be found.");
		}
		return c;
	}

	protected abstract String getExtension();

	private String getFilePath(String fileNm) {
		// データプロバイダクラスと同階層にあるテストデータファイルのURLを取得
		URL fileUrl = callerClass.getResource(fileNm);
		if (fileUrl == null) {
			fail("The file ["
					+ new File(callerClass.getResource(callerClass.getSimpleName() + ".class").getPath())
							.getParentFile().getAbsolutePath() + "/" + fileNm + "] is not exists.");
		}
		return fileUrl.getPath();
	}

	private File getDataFile(String filePath) {
		File dataFile = new File(filePath);
		if (FileUtil.isValidFile(dataFile) == false) {
			fail("The file [" + dataFile.getAbsolutePath() + "] is not a file or can not read.");
		}
		return dataFile;
	}

	private Sheet getSheet(File file, String sheetName) {
		Sheet sheet = getWorkbook(file).getSheet(sheetName);
		if (sheet == null) {
			fail("The sheet [" + sheetName + "] cannot be found.");
		}
		return sheet;
	}

	private Workbook getWorkbook(File file) {
		return new WorkbookReader().read(file);
	}

	public Iterator<Object[]> changeSheet(String sheetName) {
		return null;
	};
}
