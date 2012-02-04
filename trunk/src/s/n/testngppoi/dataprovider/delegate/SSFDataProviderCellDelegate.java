package s.n.testngppoi.dataprovider.delegate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.testng.Reporter;

import s.n.testngppoi.exception.TestNgpPoiException;

public class SSFDataProviderCellDelegate {

	private SSFDataProviderRowDelegate delegate;

	private Map<String, Object> map;

	private static final Pattern ARRAY_PUTTERN = Pattern
			.compile("^.+\\[\\d+\\]$");

	SSFDataProviderCellDelegate(SSFDataProviderRowDelegate delegate) {
		this.delegate = delegate;
		map = new HashMap<String, Object>();
	}

	public void processCell(Cell headerCell, Cell cell) throws Exception {
		String headerValue = headerCell.getRichStringCellValue().getString();
		String[] headerElements = headerValue.split(":", -1);
		switch (headerElements.length) {
		case 0:
			// String#split の仕様が変わらない限りここには入らないが･･･
			throw new TestNgpPoiException(
					"Illegal result of String#split. Result array's length must not be zero.");
		case 1:
			processCellWithNoType(headerElements[0], cell);
			break;
		case 2:
			processCellWithType(headerElements[0], headerElements[1], cell);
			break;
		default:
			throw new TestNgpPoiException(
					"There are too meny \":\" in header cell [" + headerValue
							+ "].");
		}
	}

	private void processCellWithNoType(String valiableName, Cell cell)
			throws SecurityException, IllegalArgumentException,
			NoSuchFieldException, IllegalAccessException {
		String[] valiableNameElement = valiableName.split("\\.", -1);
		switch (valiableNameElement.length) {
		case 0:
			// String#split の仕様が変わらない限りここには入らないが･･･
			throw new TestNgpPoiException(
					"Illegal result of String#split. Result array's length must not be zero.");
		case 1:
			// 単独で書いてあるものはそのまま Key-Value として入れる
			put(valiableName, getValue(cell));
			break;
		default:
			processCellWithNoType(valiableNameElement, cell);
		}
	}

	private void processCellWithNoType(String[] valiableNameElement, Cell cell)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		String parentName = valiableNameElement[0];
		Object o = map.get(parentName);
		if (o == null) {
			// 最も親のオブジェクトが無かったら無視
			return;
		}
		int len = valiableNameElement.length - 1;
		for (int i = 1; i <= len; i++) {
			String fieldName = valiableNameElement[i];
			if (ARRAY_PUTTERN.matcher(fieldName).matches()) {
				// TODO 配列・リスト・セットだった時の処理を入れる
			} else {
				Field f = o.getClass().getDeclaredField(fieldName);
				f.setAccessible(true);
				if (i == len) {
					f.set(o, getValue(cell));
				} else {
					o = f.get(o);
					if (o == null) {
						// 親のオブジェクトをたどっていく途中で無かったら無視
						return;
					}
				}
			}
		}
	}

	private void processCellWithType(String className, String valiableName,
			Cell cell) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, SecurityException,
			IllegalArgumentException, NoSuchFieldException {
		String[] valiableNameElement = valiableName.split("\\.", -1);
		switch (valiableNameElement.length) {
		case 0:
			// String#split の仕様が変わらない限りここには入らないが･･･
			throw new TestNgpPoiException(
					"Illegal result of String#split. Result array's length must not be zero.");
		case 1:
			put(valiableName, createInstance(className, cell));
			break;
		default:
			processCellWithType(className, valiableNameElement, cell);
		}
	}

	private void processCellWithType(String className,
			String[] valiableNameElement, Cell cell) throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException, ClassNotFoundException,
			InstantiationException {
		String parentName = valiableNameElement[0];
		Object o = map.get(parentName);
		if (o == null) {
			// 最も親のオブジェクトが無かったら無視
			return;
		}
		int len = valiableNameElement.length - 1;
		for (int i = 1; i <= len; i++) {
			String fieldName = valiableNameElement[i];
			if (ARRAY_PUTTERN.matcher(fieldName).matches()) {
				// TODO 配列・リスト・セットだった時の処理を入れる
			} else {
				Field f = o.getClass().getDeclaredField(fieldName);
				f.setAccessible(true);
				if (i == len) {
					f.set(o, createInstance(className, cell));
				} else {
					o = f.get(o);
					if (o == null) {
						// 親のオブジェクトをたどっていく途中で無かったら無視
						return;
					}
				}
			}
		}
	}

	private Object createInstance(String className, Cell cell)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Class<?> c = null;
		Object o = null;
		if (getValue(cell) != null) {
			c = Class.forName(className);
			// TODO クラスの属性のチェック（interfaceだったらどうするか、とか）
			// TODO 引数ありコンストラクタの場合？
			// TODO パッケージプライベートなクラスだったら？
			o = c.newInstance();
		}
		return o;
	}

	private void put(String key, Object value) {
		if (map.containsKey(key)) {
			// キーが重複していたらログには出しておく
			Reporter.log("Key [" + key + "] is duplicated.");
		}
		map.put(key, value);
	}

	protected Object getValue(Cell cell) {
		if (cell == null) {
			return processNull();
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			return processBlank(cell);
		case Cell.CELL_TYPE_STRING:
			return processString(cell);
		case Cell.CELL_TYPE_NUMERIC:
			return processNumeric(cell);
		case Cell.CELL_TYPE_BOOLEAN:
			return processBoolean(cell);
		default:
			System.out.println("There are invalid cell type in test No."
					+ delegate.getRowNum() + ", so it replaced to null. "
					+ "Cell type must be string, numeric, date or boolean.");
			return null;
		}
	}

	protected Object processNull() {
		return null;
	}

	protected Object processBlank(Cell cell) {
		return null;
	}

	protected Object processString(Cell cell) {
		String cellValue = cell.getRichStringCellValue().getString();
		if ("null".equals(cellValue)) {
			return null;
		}
		if ("\"\"".equals(cellValue)) {
			return "";
		}
		return cell.getRichStringCellValue().getString()
				.replaceAll("\\[TAB\\]", "\t");
	}

	protected Object processNumeric(Cell cell) {
		if (DateUtil.isCellDateFormatted(cell)) {
			return cell.getDateCellValue();
		}
		return Double.valueOf(cell.getNumericCellValue());
	}

	protected Object processBoolean(Cell cell) {
		return Boolean.valueOf(cell.getBooleanCellValue());
	}

	public Map<String, Object> getMap() {
		return map;
	}
}
