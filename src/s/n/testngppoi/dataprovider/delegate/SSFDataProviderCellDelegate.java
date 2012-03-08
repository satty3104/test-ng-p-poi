package s.n.testngppoi.dataprovider.delegate;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.testng.Reporter;

import s.n.testngppoi.exception.ClassUtilException;
import s.n.testngppoi.exception.TestNgpPoiException;
import s.n.testngppoi.service.CellService;
import s.n.testngppoi.service.impl.CellServiceImpl;
import s.n.testngppoi.util.ClassUtil;
import s.n.testngppoi.util.StringUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SSFDataProviderCellDelegate implements CellDelegate {

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

	private CellService cellService;

	private SSFDataProviderRowDelegate delegater;

	private Map<String, Object> map;

	public SSFDataProviderCellDelegate(SSFDataProviderRowDelegate delegater) {
		this.delegater = delegater;
		cellService = new CellServiceImpl();
		map = new HashMap<String, Object>();
	}

	@Override
	public void processCell(Cell headerCell, Cell valueCell) {
		Header h = getHeader(headerCell);
		try {
			processCell(h.className, h.valiableName, valueCell);
		} catch (ClassUtilException e) {
			// TODO Auto-generated catch block
			throw new TestNgpPoiException();
		}
	}

	private Header getHeader(Cell headerCell) {
		String headerValue = headerCell.getRichStringCellValue().getString()
				.trim();
		String[] headerElements = TYPE_VALUENAME_SEP.split(headerValue, -1);
		switch (headerElements.length) {
		case 1:
			return new Header(null, headerElements[0].trim());
		case 2:
			return new Header(headerElements[0].trim(),
					headerElements[1].trim());
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
			Cell valueCell) throws ClassUtilException {
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
			String valiableName, Cell valueCell) throws ClassUtilException {
		put(valiableName, getValue(className, valueCell));
	}

	private void processCellWithHierarchy(String className,
			String[] valiableNameElement, Cell valueCell)
			throws ClassUtilException {
		Object o = map.get(valiableNameElement[0]);
		if (o == null) {
			return;
		}
		processCellWithHierarchy(className, valiableNameElement, valueCell, 1,
				valiableNameElement.length - 1, o);
	}

	private void processCellWithHierarchy(String className,
			String[] valiableNameElement, Cell valueCell, int i, int len,
			Object o) throws ClassUtilException {
		String fieldName = valiableNameElement[i];
		Field f = ClassUtil.getAccessibleDeclaredField(fieldName, o);
		if (i == len) {
			ClassUtil.setValueToField(o, f, getValue(className, valueCell));
			return;
		}
		processCellWithHierarchy(className, valiableNameElement, valueCell,
				++i, len, ClassUtil.getObject(o, f));
	}

	private void put(String key, Object value) {
		if (map.containsKey(key)) {
			// キーが重複していたらログには出しておく
			Reporter.log("Key [" + key + "] is duplicated.");
		}
		map.put(key, value);
	}

	private Object getValue(String className, Cell valueCell)
			throws ClassUtilException {
		Object o;
		Object cellValue = cellService.getCellValue(valueCell);
		if (className == null) {
			o = cellValue;
		} else {
			o = createInstance(className, cellValue);
		}
		if (o == null) {
			return o;
		}
		if (o instanceof List) {
			return createList((List) o, (String) cellValue);
		}
		if (o instanceof Set) {
			return createSet((Set) o, (String) cellValue);
		}
		if (o instanceof Map) {
			return createMap((Map) o, (String) cellValue);
		}
		if (o.getClass().isArray()) {
			return createArray((Object[]) o, (String) cellValue);
		}
		return o;
	}

	private List createList(List l, String value) {
		return (List) createCollection(l, value);
	}

	private Set createSet(Set s, String value) {
		return (Set) createCollection(s, value);
	}

	private Collection createCollection(Collection c, String value) {
		if (StringUtil.notMatches(COLLECTION_FORMAT, value)) {
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

	private Map createMap(Map m, String value) {
		if (StringUtil.notMatches(MAP_FORMAT, value)) {
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

	private Object[] createArray(Object[] o, String value) {
		if (StringUtil.notMatches(COLLECTION_FORMAT, value)) {
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

	private Object createInstance(String className, Object cellValue)
			throws ClassUtilException {
		if (cellValue == null) {
			return null;
		}
		Object o = null;
		// TODO Enum？
		// TODO クラスの属性のチェック（interfaceだったらどうするか、とか）
		// TODO パッケージプライベートなクラスだったら？
		if (StringUtil.matches(ARRAY_CLASS_FORMAT, className)) {
			o = createArrayInstance(className);
		} else if (StringUtil.matches(CONSTRUCTER_FORMAT, className)) {
			o = createInstanceWithArgs(className);
		} else {
			o = createInstance(className);
		}
		return o;
	}

	private Object createArrayInstance(String className)
			throws ClassUtilException {
		int start = className.indexOf('[');
		int end = className.length() - 1;
		String dimensionsStr = className.substring(start + 1, end);
		String[] dimensionsStrArray = ARRAY_INDEX_SEP.split(dimensionsStr, -1);
		int len = dimensionsStrArray.length;
		int[] dimensions = new int[len];
		// "[" と "]" の間には正の数値しか入っていないことが前提（正規表現でチェックしているはず）
		// "0001" とかいう文字列でも問題ないはず
		for (int i = 0, dimension; i < len; i++) {
			dimension = Integer.parseInt(dimensionsStrArray[i]);
			if (dimension < 1) {
				// TODO
				throw new TestNgpPoiException("");
			}
			dimensions[i] = dimension;
		}
		Class c = ClassUtil.getClass(className.substring(0, start).trim());
		return Array.newInstance(c, dimensions);
	}

	private Object createInstanceWithArgs(String className)
			throws ClassUtilException {
		int start = className.indexOf('(');
		int end = className.length() - 1;
		String argsStr = className.substring(start + 1, end);
		String[] argsStrArray = COLLECTION_ELEMENT_SEP.split(argsStr, -1);
		int len = argsStrArray.length;
		Class[] types = new Class[len];
		Object[] args = new Object[len];
		for (int i = 0; i < len; i++) {
			args[i] = map.get(argsStrArray[i].trim());
			types[i] = args[i].getClass();
		}
		Class c = ClassUtil.getClass(className.substring(0, start).trim());
		Constructor con = ClassUtil.getAccessibleDeclaredConstructor(c, types);
		return ClassUtil.createInstanceFromConstructor(con, args);
	}

	private Object createInstance(String className) throws ClassUtilException {
		Class c = ClassUtil.getClass(className);
		try {
			return c.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			throw new TestNgpPoiException("");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new TestNgpPoiException("");
		}
	}

	@Override
	public Map<String, Object> getMap() {
		return map;
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
