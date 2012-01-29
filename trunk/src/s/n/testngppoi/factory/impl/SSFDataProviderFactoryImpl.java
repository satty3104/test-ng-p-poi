package s.n.testngppoi.factory.impl;

import java.io.File;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import s.n.testngppoi.dataprovider.SSFDataProvider;
import s.n.testngppoi.exception.TestNgpPoiException;
import s.n.testngppoi.factory.SSFDataProviderFactory;
import s.n.testngppoi.reader.WorkbookReader;

public class SSFDataProviderFactoryImpl implements SSFDataProviderFactory {

	private Workbook wb;

	public SSFDataProviderFactoryImpl(File file) {
		wb = new WorkbookReader().read(file);
	}

	@Override
	public Iterator<Object[]> create() {
		return create(new Throwable().getStackTrace()[1].getMethodName());
	}

	@Override
	public Iterator<Object[]> create(String sheetName) {
		if (sheetName == null) {
			throw new TestNgpPoiException(
					"The argument [sheetName] must not be null.");
		}
		return new SSFDataProvider(getSheet(sheetName));
	}

	private Sheet getSheet(String sheetName) {
		Sheet sheet = wb.getSheet(sheetName);
		if (sheet == null) {
			throw new TestNgpPoiException("The sheet [" + sheetName
					+ "] cannot be found.");
		}
		return sheet;
	}
}
