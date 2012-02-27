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
		return create(getSheetName(new Throwable()));
	}

	@Override
	public Iterator<Object[]> create(String sheetName) {
		return create(sheetName, 1);
	}

	@Override
	public Iterator<Object[]> create(String sheetName, int headerRowNum) {
		if (sheetName == null) {
			throw new TestNgpPoiException(
					"The argument [sheetName] must not be null.");
		}
		if (headerRowNum <= 0) {
			throw new TestNgpPoiException(
					"The argument [headerRowNum] must not be lower than 0.");
		}
		return _create(sheetName, headerRowNum);
	}

	private SSFDataProvider _create(String sheetName, int headerRowNum) {
		return new SSFDataProvider(getSheet(sheetName), headerRowNum);
	}

	private String getSheetName(Throwable t) {
		return t.getStackTrace()[1].getMethodName();
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
