package s.n.testngppoi.reader;

import static org.testng.Assert.fail;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import s.n.testngppoi.util.FileUtil;

public class WorkbookReader {

	public Workbook read(File file) {
		if (FileUtil.isValidFile(file) == false) {
			// throw new ;
		}
		InputStream is = null;
		Workbook wb = null;
		try {
			// POI3.8からはFileを直接渡せるらしい。。。
			wb = WorkbookFactory.create((is = new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			fail("The file [" + file.getAbsolutePath() + "] is not exists or is not a file.");
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			fail("The file [" + file.getAbsolutePath() + "]'s format is invalid.");
		} catch (IOException e) {
			e.printStackTrace();
			fail("I/O error occured. The file is [" + file.getAbsolutePath() + "].");
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
