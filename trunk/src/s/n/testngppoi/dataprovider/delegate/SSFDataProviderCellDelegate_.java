package s.n.testngppoi.dataprovider.delegate;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.testng.Reporter;

import s.n.testngppoi.exception.TestNgpPoiException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SSFDataProviderCellDelegate_ {

	private static final Pattern TYPE_VALUENAME_SEP = Pattern.compile(":");

	private static final Pattern VALUENAME_SEP = Pattern.compile("\\.");

	private static final Pattern COLLECTION_ELEMENT_SEP = Pattern.compile(",");

	private static final Pattern MAP_ENTRY_SEP = Pattern.compile("=");

	private static final Pattern ARRAY_FORMAT = Pattern.compile("^\\[.*\\]$");

	private static final Pattern MAP_FORMAT = Pattern.compile("^\\{.*\\}$");

	private SSFDataProviderRowDelegate delegater;

	private Map<String, Object> map;

	public SSFDataProviderCellDelegate_(SSFDataProviderRowDelegate delegater) {
		this.delegater = delegater;
		map = new HashMap<String, Object>();
	}

	public void processCell(Cell headerCell, Cell valueCell) throws Exception {
		Header h = getHeader(headerCell);
		processCell(h.className, h.valiableName, valueCell);
	}

	private Header getHeader(Cell headerCell) {
		String headerValue = headerCell.getRichStringCellValue().getString();
		String[] headerElements = TYPE_VALUENAME_SEP.split(headerValue, -1);
		switch (headerElements.length) {
		case 1:
			return new Header(null, headerElements[0]);
		case 2:
			return new Header(headerElements[0], headerElements[1]);
		case 0:
			// String#split の仕様が変わらない限りここには入らないが･･･
			throw new TestNgpPoiException(
					"Illegal result of String#split. Result array's length must not be zero.");
		default:
			throw new TestNgpPoiException(
					"There are too meny \":\" in header cell [" + headerValue
							+ "].");
		}
	}

	private void processCell(String className, String valiableName,
			Cell valueCell) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		String[] valiableNameElement = VALUENAME_SEP.split(valiableName, -1);
		switch (valiableNameElement.length) {
		case 0:
			// String#split の仕様が変わらない限りここには入らないが･･･
			throw new TestNgpPoiException(
					"Illegal result of String#split. Result array's length must not be zero.");
		case 1:
			saiki1(className, valiableName, valueCell);
			break;
		default:
		}
	}

	private void saiki1(String className, String valiableName, Cell valueCell)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Object o = getValue(className, valueCell);
		Object value;
		if (o instanceof List) {
			value = processList((List) o, valiableName, valueCell);
		} else if (o instanceof Set) {
			value = processSet((Set) o, valiableName, valueCell);
		} else if (o instanceof Map) {
			value = processMap((Map) o, valiableName, valueCell);
		} else if (o.getClass().isArray()) {
			// TODO 配列の場合
			value = null;
		} else {
			value = o;
		}
		put(valiableName, value);
	}

	private List processList(List list, String valiableName, Cell valueCell) {
		return (List) processCollection(list, valiableName, valueCell);
	}

	private Set processSet(Set set, String valiableName, Cell valueCell) {
		return (Set) processCollection(set, valiableName, valueCell);
	}

	private Collection processCollection(Collection c, String valiableName,
			Cell valueCell) {
		Object obj = getCellValue(valueCell);
		if (obj instanceof String == false) {
			// TODO
			throw new TestNgpPoiException("");
		}
		String value = (String) obj;
		if (ARRAY_FORMAT.matcher(value).matches() == false) {
			return null;
		}
		String[] elements = COLLECTION_ELEMENT_SEP.split(
				value.substring(1, value.length() - 1), -1);
		for (String s : elements) {
			// TODO NULLだったら？
			c.add(map.get(s));
		}
		return c;
	}

	private Map processMap(Map _map, String valiableName, Cell valueCell) {
		Object obj = getCellValue(valueCell);
		if (obj instanceof String == false) {
			// TODO
			throw new TestNgpPoiException("");
		}
		String value = (String) obj;
		if (MAP_FORMAT.matcher(value).matches() == false) {
			return null;
		}
		String[] elements = COLLECTION_ELEMENT_SEP.split(
				value.substring(1, value.length() - 1), -1);
		for (String entry : elements) {
			String[] el = MAP_ENTRY_SEP.split(entry, -1);
			switch (el.length) {
			// TODO NULLだったら？
			case 1:
				_map.put(map.get(el[0]), null);
				break;
			case 2:
				_map.put(map.get(el[0]), map.get(el[1]));
				break;
			default:
				// TODO
				throw new TestNgpPoiException("");
			}
		}
		return _map;
	}

	private void saiki(String className, Object o, String valiableName,
			Cell valueCell) {

	}

	private Object getValue(String className, Cell cell)
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
		// TODO 他に特殊文字は？
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

	private static class Header {
		String className;
		String valiableName;

		Header(String className, String valiableName) {
			this.className = className;
			this.valiableName = valiableName;
		}
	}
}
