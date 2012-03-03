package s.n.testngppoi.dataprovider.delegate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.testng.Reporter;

import s.n.testngppoi.exception.TestNgpPoiException;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SSFDataProviderCellDelegate {

	private SSFDataProviderRowDelegate delegater;

	private Map<String, Object> map;

	private static final Pattern ARRAY_PUTTERN = Pattern
			.compile("^.+\\[\\d+\\]$");

	private static final Pattern MAP_PUTTERN = Pattern.compile("^.+\\{.+\\}$");

	private static final String TYPE_VALUENAME_SEP = ":";

	private static final String VALUENAME_SEP = "\\.";

	public SSFDataProviderCellDelegate(SSFDataProviderRowDelegate delegater) {
		this.delegater = delegater;
		map = new HashMap<String, Object>();
	}

	public void processCell(Cell headerCell, Cell valueCell) throws Exception {
		String headerValue = headerCell.getRichStringCellValue().getString();
		String[] headerElements = headerValue.split(TYPE_VALUENAME_SEP, -1);
		String className;
		String valiableName;
		switch (headerElements.length) {
		case 0:
			// String#split の仕様が変わらない限りここには入らないが･･･
			throw new TestNgpPoiException(
					"Illegal result of String#split. Result array's length must not be zero.");
		case 1:
			className = null;
			valiableName = headerElements[0];
			break;
		case 2:
			className = headerElements[0];
			valiableName = headerElements[1];
			break;
		default:
			throw new TestNgpPoiException(
					"There are too meny \":\" in header cell [" + headerValue
							+ "].");
		}
		processCell(className, valiableName, valueCell);
	}

	public void processCell(String className, String valiableName,
			Cell valueCell) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, SecurityException,
			IllegalArgumentException, NoSuchFieldException {
		String[] valiableNameElement = valiableName.split(VALUENAME_SEP, -1);
		switch (valiableNameElement.length) {
		case 0:
			// String#split の仕様が変わらない限りここには入らないが･･･
			throw new TestNgpPoiException(
					"Illegal result of String#split. Result array's length must not be zero.");
		case 1:
			processCellWithNoHierarchy(className, valiableName, valueCell);
			break;
		default:
			processCellWithHierarchy(className, valiableNameElement, valueCell);
		}
	}

	private void processCellWithNoHierarchy(String className,
			String valiableName, Cell cell) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		int open;
		int close;
		if (isArray(valiableName)) {
			open = valiableName.indexOf("[");
			close = valiableName.indexOf("]");
		} else if (isMap(valiableName)) {
			open = valiableName.indexOf("{");
			close = valiableName.indexOf("}");
		} else {
			// ListでもMapでもなく単独で書いてあるものはそのまま Key-Value として入れる
			put(valiableName, getValue(className, cell));
			return;
		}
		Object o = map.get(valiableName.substring(0, open));
		if (o == null) {
			return;
		}
		if (o instanceof List) {
			((List) o).add(getInt(valiableName.substring(open + 1, close)),
					getValue(className, cell));
			return;
		}
		if (o instanceof Set) {
			((Set) o).add(getValue(className, cell));
			return;
		}
		// TODO 配列の場合？
		if (o instanceof Map) {
			((Map) o).put(map.get(valiableName.substring(open + 1, close)),
					getValue(className, cell));
			return;
		}
		// ここどうしようかな、、、
	}

	private void processCellWithHierarchy(String className,
			String[] valiableNameElement, Cell cell) throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException, ClassNotFoundException,
			InstantiationException {
		Object o = null;
		// TODO ここ再帰でいけるはず
		for (int i = 0, len = valiableNameElement.length - 1; i <= len; i++) {
			String valiableName = valiableNameElement[i];
			int open;
			int close;
			String fieldName;
			if (isArray(valiableName)) {
				open = valiableName.indexOf("[");
				close = valiableName.indexOf("]");
				fieldName = valiableName.substring(0, open);
			} else if (isMap(valiableName)) {
				open = valiableName.indexOf("{");
				close = valiableName.indexOf("}");
				fieldName = valiableName.substring(0, open);
			} else {
				if (i == 0) {
					o = map.get(valiableName);
					if (o == null) {
						// 最も親のオブジェクトが無かったら無視
						return;
					}
				} else {
					Field f = o.getClass().getDeclaredField(valiableName);
					f.setAccessible(true);
					if (i == len) {
						f.set(o, getValue(className, cell));
					} else {
						o = f.get(o);
						if (o == null) {
							// 親のオブジェクトをたどっていく途中で無かったら無視
							return;
						}
					}
				}
				return;
			}
			if (i == 0) {
				o = map.get(fieldName);
			} else {
				Field f = o.getClass().getDeclaredField(fieldName);
				f.setAccessible(true);
				o = f.get(o);
			}
			if (o == null) {
				// 親のオブジェクトをたどっていく途中で無かったら無視
				return;
			}
			if (i == len) {
				if (o instanceof List) {
					((List) o).add(
							getInt(valiableName.substring(open + 1, close)),
							getValue(className, cell));
				} else if (o instanceof Set) {
					((Set) o).add(getValue(className, cell));
				} else if (o instanceof Map) {
					((Map) o).put(
							map.get(valiableName.substring(open + 1, close)),
							getValue(className, cell));
				} else {
					// ここどうしようかな、、、
				}
			} else {
				if (o instanceof List) {
					o = ((List) o).get(getInt(valiableName.substring(open + 1,
							close)));
				} else if (o instanceof Set) {
					// ここどうしようかな、、、
				} else if (o instanceof Map) {
					o = ((Map) o).get(map.get(valiableName.substring(open + 1,
							close)));
				} else {
					// ここどうしようかな、、、
				}
			}
		}
	}

	protected Object getValue(String className, Cell cell)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		if (className == null) {
			return getCellValue(cell);
		}
		return createInstance(className, cell);
	}

	private Object createInstance(String className, Cell cell)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Class<?> c = null;
		Object o = null;
		if (getCellValue(cell) != null) {
			c = Class.forName(className);
			// TODO クラスの属性のチェック（interfaceだったらどうするか、とか）
			// TODO 引数ありコンストラクタの場合？
			// TODO パッケージプライベートなクラスだったら？
			// TODO 配列だったら？
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

	private boolean isMap(String valiableName) {
		return MAP_PUTTERN.matcher(valiableName).matches();
	}

	private boolean isArray(String valiableName) {
		return ARRAY_PUTTERN.matcher(valiableName).matches();
	}

	private int getInt(String i) {
		return Integer.valueOf(i);
	}

	protected Object getCellValue(Cell cell) {
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
			Reporter.log("There are invalid cell type in test No."
					+ delegater.getRowNum() + ", so it replaced to null. "
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

	private void print(Object o) {
		System.out.println(o);
	}
}
