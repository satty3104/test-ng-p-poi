package s.n.testngppoi.iterator;

import static org.testng.Assert.fail;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public abstract class AbstractExcelDataProvider implements Iterator<Object[]> {

	protected String fileName;

	protected String sheetName;

	@SuppressWarnings("rawtypes")
	protected Class callerClass;

	protected Iterator<Row> rowIterator;

	protected Row header;

	protected int columnNum;

	protected StackTraceElement getCaller(Throwable t) {
		StackTraceElement[] stackTraceElements = t.getStackTrace();
		if (stackTraceElements.length == 0) {
			fail("Cannot get the array of StackTraceElement.");
		}
		if (stackTraceElements.length == 1) {
			return null;
		}
		return stackTraceElements[1];
	}

	protected void initCallerClass(StackTraceElement elem) {
		if (callerClass != null) {
			return;
		}
		if (elem == null) {
			callerClass = this.getClass();
			return;
		}
		try {
			callerClass = Class.forName(elem.getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("The caller class [" + elem.getClassName()
					+ "] cannot be found.");
		}
	}

	protected void init(String fileNm, String sheetName) {
		Sheet sheet = getWorkbook(getDataFile(fileNm)).getSheet(sheetName);
		if (sheet == null) {
			fail("The sheet [" + sheetName + "] cannot be found.");
		}
		rowIterator = sheet.rowIterator();
		if (rowIterator.hasNext() == false) {
			// ヘッダ行がない場合は失敗に
			fail("There is no header row in the sheet [" + sheetName + "].");
		}
		header = rowIterator.next();
		if (rowIterator.hasNext() == false) {
			// ヘッダ行しかない場合は成功にするがログには出しておく
			System.out.println("There is no test in the sheet [" + sheetName
					+ "].");
		}
		columnNum = header.getLastCellNum();
	}

	private File getDataFile(String fileNm) {
		// データプロバイダクラスと同階層にあるテストデータファイルのURLを取得
		URL fileUrl = callerClass.getResource(fileNm);
		if (fileUrl == null) {
			String classNm = callerClass.getSimpleName() + ".class";
			URL classUrl = callerClass.getResource(classNm);
			File dir = new File(classUrl.getPath()).getParentFile();
			String dirPath = dir.getAbsolutePath();
			fail("The file [" + dirPath + "/" + fileNm + "] is not exists.");
		}
		File dataFile = new File(fileUrl.getPath());
		if (isValidFile(dataFile) == false) {
			fail("The file [" + dataFile.getAbsolutePath()
					+ "] is not a file or can not read.");
		}
		return dataFile;
	}

	private boolean isValidFile(File f) {
		return f.isFile() && f.canRead();
	}

	private Workbook getWorkbook(File f) {
		InputStream is = null;
		Workbook wb = null;
		try {
			// POI3.8からはFileを直接渡せるらしい。。。
			wb = WorkbookFactory.create((is = new FileInputStream(f)));
		} catch (FileNotFoundException e) {
			fail("The file [" + f.getAbsolutePath()
					+ "] is not exists or is not a file.");
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			fail("The file [" + f.getAbsolutePath() + "]'s format is invalid.");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Error occured. The file is [" + f.getAbsolutePath() + "].");
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

	@Override
	public boolean hasNext() {
		return rowIterator.hasNext();
	}

	@Override
	public Object[] next() {
		Row row = rowIterator.next();
		if (row.getLastCellNum() > columnNum) {
			// カラムが多すぎる場合は失敗にしないがログには出しておく
			System.out.println("There are too many columns in test No."
					+ row.getRowNum() + ".");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Iterator<Cell> cellIterator = row.cellIterator();
		for (int i = 0; i < columnNum; i++) {
			Cell headerCell = header.getCell(i);
			if (headerCell == null) {
				// ヘッダが未入力の場合は失敗
				fail("The cell with no value is found in the header row.");
			}
			if (headerCell.getCellType() != Cell.CELL_TYPE_STRING) {
				// ヘッダのデータ型が文字列でない場合は失敗
				fail("Header row's cell must be String type.");
			}
			if (cellIterator.hasNext() == false) {
				map.put(headerCell.getRichStringCellValue().getString(), null);
				continue;
			}
			Cell cell = cellIterator.next();
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				map.put(headerCell.getRichStringCellValue().getString(), null);
				break;
			case Cell.CELL_TYPE_STRING:
				String cellValue = cell.getRichStringCellValue().getString();
				if ("null".equals(cellValue)) {
					map.put(headerCell.getRichStringCellValue().getString(),
							null);
					break;
				}
				if ("\"\"".equals(cellValue)) {
					map.put(headerCell.getRichStringCellValue().getString(), "");
					break;
				}
				map.put(headerCell.getRichStringCellValue().getString(), cell
						.getRichStringCellValue().getString());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					map.put(headerCell.getRichStringCellValue().getString(),
							cell.getDateCellValue());
				} else {
					map.put(headerCell.getRichStringCellValue().getString(),
							cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				map.put(headerCell.getRichStringCellValue().getString(),
						cell.getBooleanCellValue());
				break;
			default:
				System.out.println("There are invalid cell type in test No."
						+ row.getRowNum() + ", so it replaced to null.");
				map.put(headerCell.getRichStringCellValue().getString(), null);
			}
		}

		return new Object[] { map };
	}

	@Override
	public void remove() {
	}
}
