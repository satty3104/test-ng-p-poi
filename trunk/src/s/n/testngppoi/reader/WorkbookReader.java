package s.n.testngppoi.reader;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import s.n.testngppoi.exception.TestNgpPoiException;
import s.n.testngppoi.util.FileUtil;

public class WorkbookReader {

	public Workbook read(final File file) {
		if (FileUtil.isInvalid(file)) {
			throw new TestNgpPoiException("The file [" + file.getAbsolutePath()
					+ "] is invalid.");
		}
		InputStream is = null;
		try {
			// POI3.8からはFileを直接渡せるらしい。。。
			return WorkbookFactory.create((is = new FileInputStream(file)));
		} catch (final FileNotFoundException e) {
			throw new TestNgpPoiException("The file [" + file.getAbsolutePath()
					+ "] is not exists or is not a file.", e);
		} catch (final InvalidFormatException e) {
			throw new TestNgpPoiException("The file [" + file.getAbsolutePath()
					+ "]'s format is invalid.", e);
		} catch (final IOException e) {
			throw new TestNgpPoiException("I/O error occured. The file is ["
					+ file.getAbsolutePath() + "].", e);
		} finally {
			close(is);
		}
	}

	private void close(final Closeable c) {
		if (c == null) {
			return;
		}
		try {
			c.close();
		} catch (final IOException e) {
			// TODO
			System.out.println("Failed to close stream.");
		}
	}
}
