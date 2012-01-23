package s.n.testngppoi.factory.impl;

import static org.testng.Assert.fail;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import s.n.testngppoi.factory.SSFDataProviderFactory;
import s.n.testngppoi.iterator.SSFDataProvider;

public abstract class AbstractSSFDataProviderFactory implements
		SSFDataProviderFactory {

	@SuppressWarnings("rawtypes")
	private Class callerClass;

	@Override
	public Iterator<Object[]> create() {
		StackTraceElement caller = new Throwable().getStackTrace()[1];
		callerClass = getCallerClass(caller);
		return create(callerClass.getSimpleName() + getExtension(),
				caller.getMethodName());
	}

	@Override
	public Iterator<Object[]> create(String fileName, String sheetName) {
		if (callerClass == null) {
			callerClass = getCallerClass(new Throwable().getStackTrace()[1]);
		}
		return create(getDataFile(getFilePath(fileName)), sheetName);
	}

	@Override
	public Iterator<Object[]> create(String filePath, String fileName,
			String sheetName) {
		return create(getDataFile(filePath + File.separator + fileName),
				sheetName);
	}

	private Iterator<Object[]> create(File file, String sheetName) {
		return new SSFDataProvider(getSheet(file, sheetName));
	}

	@SuppressWarnings("rawtypes")
	private Class getCallerClass(StackTraceElement elem) {
		Class c = null;
		try {
			c = Class.forName(elem.getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("The caller class [" + elem.getClassName()
					+ "] cannot be found.");
		}
		return c;
	}

	protected abstract String getExtension();

	private String getFilePath(String fileNm) {
		// データプロバイダクラスと同階層にあるテストデータファイルのURLを取得
		URL fileUrl = callerClass.getResource(fileNm);
		if (fileUrl == null) {
			fail("The file ["
					+ new File(callerClass.getResource(
							callerClass.getSimpleName() + ".class").getPath())
							.getParentFile().getAbsolutePath() + "/" + fileNm
					+ "] is not exists.");
		}
		return fileUrl.getPath();
	}

	private File getDataFile(String filePath) {
		File dataFile = new File(filePath);
		if (isValidFile(dataFile) == false) {
			fail("The file [" + dataFile.getAbsolutePath()
					+ "] is not a file or can not read.");
		}
		return dataFile;
	}

	private boolean isValidFile(File f) {
		return f.isFile() && f.canRead();
	}

	private Sheet getSheet(File file, String sheetName) {
		Sheet sheet = getWorkbook(file).getSheet(sheetName);
		if (sheet == null) {
			fail("The sheet [" + sheetName + "] cannot be found.");
		}
		return sheet;
	}

	private Workbook getWorkbook(File file) {
		InputStream is = null;
		Workbook wb = null;
		try {
			// POI3.8からはFileを直接渡せるらしい。。。
			wb = WorkbookFactory.create((is = new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			fail("The file [" + file.getAbsolutePath()
					+ "] is not exists or is not a file.");
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			fail("The file [" + file.getAbsolutePath()
					+ "]'s format is invalid.");
		} catch (IOException e) {
			e.printStackTrace();
			fail("I/O error occured. The file is [" + file.getAbsolutePath()
					+ "].");
		} finally {
			close(is);
		}
		return wb;
	}

	private void close(Closeable c) {
		if (c == null) {
			return;
		}
		try {
			c.close();
		} catch (IOException e) {
			System.out.println("Failed to close stream.");
		}
	}
}
