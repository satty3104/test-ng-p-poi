package s.n.testngppoi.dataprovider.delegate;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

	private static final Pattern ARRAY_INDEX_SEP = Pattern.compile("\\]\\[");

	private static final Pattern MAP_ENTRY_SEP = Pattern.compile("=");

	private static final Pattern COLLECTION_FORMAT = Pattern
			.compile("^\\[.*\\]$");

	private static final Pattern MAP_FORMAT = Pattern.compile("^\\{.*\\}$");

	private static final Pattern ARRAY_CLASS_FORMAT = Pattern
			.compile("^.+(\\[\\d+\\])+$");

	private static final Pattern CONSTRUCTER_FORMAT = Pattern
			.compile("^.+\\(.+\\)$");

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
			Cell valueCell) throws Exception {
		String[] valiableNameElement = VALUENAME_SEP.split(valiableName, -1);
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
			String valiableName, Cell valueCell) throws Exception {
		put(valiableName, getValue(className, valueCell));
	}

	private void processCellWithHierarchy(String className,
			String[] valiableNameElement, Cell valueCell) throws Exception {
		Object o = map.get(valiableNameElement[0]);
		if (o == null) {
			return;
		}
		for (int i = 1, len = valiableNameElement.length - 1; i <= len; i++) {
			if (i == len) {
				Object value = getValue(className, valueCell);
				Field f = o.getClass().getDeclaredField(valiableNameElement[i]);
				f.setAccessible(true);
				f.set(o, value);
			}
			// TODO!!
		}
	}

	private Object getValue(String className, Cell valueCell) throws Exception {
		Object o;
		if (className == null) {
			o = getCellValue(valueCell);
		} else {
			o = createInstance(className, valueCell);
		}
		if (o == null) {
			return o;
		}
		if (o instanceof List) {
			return processList((List) o, valueCell);
		}
		if (o instanceof Set) {
			return processSet((Set) o, valueCell);
		}
		if (o instanceof Map) {
			return processMap((Map) o, valueCell);
		}
		if (o.getClass().isArray()) {
			return processArray((Object[]) o, valueCell);
		}
		return o;
	}

	private List processList(List l, Cell valueCell) {
		return (List) processCollection(l, valueCell);
	}

	private Set processSet(Set s, Cell valueCell) {
		return (Set) processCollection(s, valueCell);
	}

	private Collection processCollection(Collection c, Cell valueCell) {
		String value = (String) processString(valueCell);
		if (notMatches(COLLECTION_FORMAT, value)) {
			return null;
		}
		String elements = value.substring(1, value.length() - 1).trim();
		if (elements.length() == 0) {
			return c;
		}
		for (String s : COLLECTION_ELEMENT_SEP.split(elements, -1)) {
			// TODO NULLだったら？
			c.add(map.get(s.trim()));
		}
		return c;
	}

	private Map processMap(Map m, Cell valueCell) {
		String value = (String) processString(valueCell);
		if (notMatches(MAP_FORMAT, value)) {
			return null;
		}
		String elements = value.substring(1, value.length() - 1).trim();
		if (elements.length() == 0) {
			return m;
		}
		for (String entry : COLLECTION_ELEMENT_SEP.split(elements, -1)) {
			String[] el = MAP_ENTRY_SEP.split(entry.trim(), -1);
			switch (el.length) {
			// TODO NULLだったら？
			case 2:
				m.put(map.get(el[0].trim()), map.get(el[1].trim()));
				break;
			default:
				// TODO
				throw new TestNgpPoiException("");
			}
		}
		return m;
	}

	private Object[] processArray(Object[] o, Cell valueCell) {
		String value = (String) processString(valueCell);
		if (notMatches(COLLECTION_FORMAT, value)) {
			return null;
		}
		String[] elements = COLLECTION_ELEMENT_SEP.split(
				value.substring(1, value.length() - 1), -1);
		for (int i = 0; i < elements.length; i++) {
			// TODO NULLだったら？
			// TODO IndexOutOfBoundsException
			// TODO ArrayStoreException
			o[i] = map.get(elements[i].trim());
		}
		return o;
	}

	private Object createInstance(String className, Cell cell)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			SecurityException, InvocationTargetException, NoSuchMethodException {
		if (getCellValue(cell) == null) {
			return null;
		}
		Object o = null;
		// TODO Enum？
		// TODO クラスの属性のチェック（interfaceだったらどうするか、とか）
		// TODO パッケージプライベートなクラスだったら？
		if (matches(ARRAY_CLASS_FORMAT, className)) {
			int start = className.indexOf('[');
			int end = className.length() - 1;
			String[] dimensionsStrArray = ARRAY_INDEX_SEP.split(
					className.substring(start + 1, end), -1);
			int len = dimensionsStrArray.length;
			int[] dimensions = new int[len];
			for (int i = 0; i < len; i++) {
				dimensions[i] = Integer.parseInt(dimensionsStrArray[i].trim());
			}
			Class c = Class.forName(className.substring(0, start));
			o = Array.newInstance(c, dimensions);
		} else if (CONSTRUCTER_FORMAT.matcher(className).matches()) {
			int start = className.indexOf('(');
			int end = className.length() - 1;
			String[] argsStrArray = COLLECTION_ELEMENT_SEP.split(
					className.substring(start + 1, end), -1);
			int len = argsStrArray.length;
			Class[] types = new Class[len];
			Object[] args = new Object[len];
			for (int i = 0; i < len; i++) {
				args[i] = map.get(argsStrArray[i].trim());
				types[i] = args[i].getClass();
			}
			Class c = Class.forName(className.substring(0, start));
			o = c.getConstructor(types).newInstance(args);
		} else {
			Class c = Class.forName(className);
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

	private boolean matches(Pattern p, String target) {
		return p.matcher(target).matches();
	}

	private boolean notMatches(Pattern p, String target) {
		return matches(p, target) == false;
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
