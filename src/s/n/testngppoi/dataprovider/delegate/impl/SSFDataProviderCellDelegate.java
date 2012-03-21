package s.n.testngppoi.dataprovider.delegate.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;

import s.n.testngppoi.dataprovider.delegate.CellDelegate;
import s.n.testngppoi.dataprovider.delegate.RowDelegate;
import s.n.testngppoi.exception.ClassUtilException;
import s.n.testngppoi.exception.InvalidCellTypeException;
import s.n.testngppoi.exception.TestNgpPoiException;
import s.n.testngppoi.service.CellService;
import s.n.testngppoi.service.LogService;
import s.n.testngppoi.service.impl.CellServiceImpl;
import s.n.testngppoi.util.ClassUtil;
import s.n.testngppoi.util.LogUtil;
import s.n.testngppoi.util.StringUtil;

/**
 * Spread Sheet Format (SSF) 形式のファイルのセルに関する処理を行うクラス。
 * 
 * @author s_nagai
 * @since 2012/02/04
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SSFDataProviderCellDelegate implements CellDelegate {

	/** ログ出力を行うクラスのインスタンス */
	private static final LogService log = LogUtil.getLogger();

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

	private static final Map<String, String> TYPE_ENCODING;

	static {
		final Map<String, String> typeEncoding = new HashMap<String, String>() {
			private static final long serialVersionUID = -1214320567853917341L;
			{
				put("byte", "B");
				put("short", "S");
				put("int", "I");
				put("long", "J");
				put("float", "F");
				put("double", "D");
				put("char", "C");
				put("boolean", "Z");
			}
		};
		TYPE_ENCODING = Collections.unmodifiableMap(typeEncoding);
	}

	private CellService cellService;

	private RowDelegate delegater;

	private Map<String, Object> map;

	private Map<String, Class> tmpMap;

	public SSFDataProviderCellDelegate(final RowDelegate delegater) {
		this.delegater = delegater;
		cellService = new CellServiceImpl();
		map = new HashMap<String, Object>();
		tmpMap = new HashMap<String, Class>();
	}

	@Override
	public void processCell(final Cell headerCell, final Cell valueCell) {
		final Header h = getHeader(headerCell);
		try {
			processCell(h.className, h.valiableNameHierarchy, valueCell);
		} catch (ClassUtilException e) {
			// TODO Auto-generated catch block
			throw new TestNgpPoiException();
		}
	}

	private Header getHeader(final Cell headerCell) {
		// TODO ヘッダをキャッシュする
		final String headerValue = headerCell.getRichStringCellValue()
				.getString().trim();
		String[] headerElements = TYPE_VALUENAME_SEP.split(headerValue, -1);
		final String[] valiableNameHierarchy;
		switch (headerElements.length) {
		case 1:
			valiableNameHierarchy = VALUENAME_SEP.split(headerElements[0], -1);
			return new Header(null, valiableNameHierarchy);
		case 2:
			valiableNameHierarchy = VALUENAME_SEP.split(headerElements[1], -1);
			return new Header(headerElements[0].trim(), valiableNameHierarchy);
		case 0:
			// String#split の仕様が変わらない限りここには入らないが･･･
			throw new TestNgpPoiException(
					"Illegal result of String#split. Result array's length must not be zero.");
		default:
			throw new TestNgpPoiException(
					"There are too meny \":\"s in header cell [" + headerValue
							+ "].");
		}
	}

	private void processCell(final String className,
			final String[] valiableNameHierarchy, final Cell valueCell)
			throws ClassUtilException {
		switch (valiableNameHierarchy.length) {
		case 0:
			// String#split の仕様が変わらない限りここには入らないが･･･
			throw new TestNgpPoiException(
					"Illegal result of String#split. Result array's length must not be zero.");
		case 1:
			processCellWithNoHierarchy(className,
					valiableNameHierarchy[0].trim(), valueCell);
			break;
		default:
			processCellWithHierarchy(className, valiableNameHierarchy,
					valueCell);
		}
	}

	private void processCellWithNoHierarchy(final String className,
			final String key, final Cell valueCell) throws ClassUtilException {
		put(key, getValue(className, valueCell));
		putTmp(key, className);
	}

	private void put(final String key, final Object value) {
		if (map.containsKey(key)) {
			// キーが重複していたらログには出しておく
			log.log("Key [" + key + "] is duplicated.");
		}
		map.put(key, value);
	}

	private void putTmp(final String key, final String className)
			throws ClassUtilException {
		final Class value;
		if (StringUtil.matches(ARRAY_CLASS_FORMAT, className)) {
			final int start = className.indexOf('[');
			final int end = className.length() - 1;
			final String dimensionsStr = className.substring(start + 1, end);
			final int len = ARRAY_INDEX_SEP.split(dimensionsStr, -1).length;
			String name = "";
			for (int i = 0; i < len; i++) {
				name = "[" + name;
			}
			final Class c = ClassUtil.getClass(className.substring(0, start));
			if (c.isPrimitive()) {
				name = name + TYPE_ENCODING.get(c.getName());
			} else {
				name = name + "L" + c.getName() + ";";
			}
			value = ClassUtil.getClass(name);
		} else if (StringUtil.matches(CONSTRUCTER_FORMAT, className)) {
			value = ClassUtil.getClass(className.substring(0,
					className.indexOf('(')));
		} else {
			value = ClassUtil.getClass(className);
		}
		tmpMap.put(key, value);
	}

	private void processCellWithHierarchy(final String className,
			final String[] hierarchy, final Cell valueCell)
			throws ClassUtilException {
		Object o = map.get(hierarchy[0].trim());
		if (o == null) {
			return;
		}
		final int len = hierarchy.length - 1;
		Field f;
		for (int i = 1; i <= len; i++) {
			f = ClassUtil.getAccessibleDeclaredField(hierarchy[i].trim(), o);
			if (i != len) {
				o = ClassUtil.getObject(o, f);
				continue;
			}
			ClassUtil.setValueToField(o, f, getValue(className, valueCell));
		}
	}

	private Object getValue(final String className, final Cell valueCell)
			throws ClassUtilException {
		final Object cellValue;
		try {
			cellValue = cellService.getCellValue(valueCell);
		} catch (final InvalidCellTypeException e) {
			throw new TestNgpPoiException(
					"There are invalid cell type in test No."
							+ delegater.getRowNum()
							+ ", so it replaced to null. "
							+ "Cell type must be string, numeric, date or boolean.",
					e);
		}
		// クラスが指定されていなかったらそのまま（Cellの型に依存する）
		if (className == null) {
			return cellValue;
		}
		// 以下はクラス指定ありなので、まずインスタンスを生成する
		final Object o = createInstance(className, cellValue);
		if (o == null) {
			return o;
		}
		// インスタンスがコレクション系であれば中に値を詰める
		if (o instanceof List) {
			return createList((List) o, cellValue);
		}
		if (o instanceof Set) {
			return createSet((Set) o, cellValue);
		}
		if (o instanceof Map) {
			return createMap((Map) o, cellValue);
		}
		if (o.getClass().isArray()) {
			return createArray((Object[]) o, cellValue);
		}
		// コレクション系でなければそのまま
		return o;
	}

	private Object createInstance(final String className, final Object cellValue)
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

	private Object createArrayInstance(final String className)
			throws ClassUtilException {
		final int start = className.indexOf('[');
		final int end = className.length() - 1;
		final String dimensionsStr = className.substring(start + 1, end);
		final String[] dimensionsStrArray = ARRAY_INDEX_SEP.split(
				dimensionsStr, -1);
		final int len = dimensionsStrArray.length;
		final int[] dimensions = new int[len];
		for (int i = 0, dimension; i < len; i++) {
			// "[" と "]" の間には正の数値しか入っていないことが前提（正規表現でチェックしているはず）
			// "0001" とかいう文字列でも問題ないはず
			dimension = Integer.parseInt(dimensionsStrArray[i]);
			if (dimension < 1) {
				throw new TestNgpPoiException("Illegal value [" + dimension
						+ "] is specified for dimension of Array.");
			}
			dimensions[i] = dimension;
		}
		final Class c = ClassUtil.getClass(className.substring(0, start));
		return Array.newInstance(c, dimensions);
	}

	private Object createInstanceWithArgs(final String className)
			throws ClassUtilException {
		final int start = className.indexOf('(');
		final int end = className.length() - 1;
		final String argsStr = className.substring(start + 1, end);
		final String[] argsStrArray = COLLECTION_ELEMENT_SEP.split(argsStr, -1);
		final int len = argsStrArray.length;
		final Class[] types = new Class[len];
		final Object[] args = new Object[len];
		for (int i = 0; i < len; i++) {
			final String key = argsStrArray[i].trim();
			final Object arg = map.get(key);
			args[i] = arg;
			if (arg == null) {
				types[i] = tmpMap.get(key);
			} else {
				types[i] = arg.getClass();
			}
		}
		final Class c = ClassUtil.getClass(className.substring(0, start));
		final Constructor con = ClassUtil.getAccessibleDeclaredConstructor(c,
				types);
		return ClassUtil.createInstanceFromConstructor(con, args);
	}

	private Object createInstance(final String className)
			throws ClassUtilException {
		final Class c = ClassUtil.getClass(className);
		return ClassUtil.createInstance(c);
	}

	private List createList(final List l, final Object cellValue) {
		if (cellValue instanceof String == false) {
			throw new TestNgpPoiException(
					"Cell value must be instance of String when create an instance of List in test No."
							+ delegater.getRowNum() + ".");
		}
		return (List) createCollection(l, cellValue);
	}

	private Set createSet(final Set s, final Object cellValue) {
		if (cellValue instanceof String == false) {
			throw new TestNgpPoiException(
					"Cell value must be instance of String when create an instance of Set in test No."
							+ delegater.getRowNum() + ".");
		}
		return (Set) createCollection(s, cellValue);
	}

	private Collection createCollection(final Collection c,
			final Object cellValue) {
		final String value = (String) cellValue;
		if (StringUtil.notMatches(COLLECTION_FORMAT, value)) {
			return null;
		}
		final String elements = value.substring(1, value.length() - 1);
		if (elements.length() == 0) {
			return c;
		}
		for (final String s : COLLECTION_ELEMENT_SEP.split(elements, -1)) {
			c.add(map.get(s.trim()));
		}
		return c;
	}

	private Map createMap(final Map m, final Object cellValue) {
		if (cellValue instanceof String == false) {
			throw new TestNgpPoiException(
					"Cell value must be instance of String when create an instance of Map in test No."
							+ delegater.getRowNum() + ".");
		}
		final String value = (String) cellValue;
		if (StringUtil.notMatches(MAP_FORMAT, value)) {
			return null;
		}
		final String elements = value.substring(1, value.length() - 1);
		if (elements.length() == 0) {
			return m;
		}
		for (final String entry : COLLECTION_ELEMENT_SEP.split(elements, -1)) {
			final String[] el = MAP_ENTRY_SEP.split(entry, -1);
			switch (el.length) {
			case 2:
				m.put(map.get(el[0].trim()), map.get(el[1].trim()));
				break;
			default:
				throw new TestNgpPoiException(
						"There are too meny \"=\"s in cell value in test No."
								+ delegater.getRowNum() + ".");
			}
		}
		return m;
	}

	private Object[] createArray(final Object[] o, final Object cellValue) {
		if (cellValue instanceof String == false) {
			throw new TestNgpPoiException(
					"Cell value must be instance of String when create an instance of Array in test No."
							+ delegater.getRowNum() + ".");
		}
		final String value = (String) cellValue;
		if (StringUtil.notMatches(COLLECTION_FORMAT, value)) {
			return null;
		}
		final String elementsStr = value.substring(1, value.length() - 1);
		if (elementsStr.length() == 0) {
			return o;
		}
		final String[] elements = COLLECTION_ELEMENT_SEP.split(elementsStr, -1);
		final int len = elements.length;
		if (len != o.length) {
			throw new TestNgpPoiException(
					"The number of elements in the cell is not equal to length of array in test No."
							+ delegater.getRowNum() + ".");
		}
		for (int i = 0; i < len; i++) {
			// TODO ArrayStoreException
			o[i] = map.get(elements[i].trim());
		}
		return o;
	}

	@Override
	public Map<String, Object> getMap() {
		return map;
	}

	/**
	 * ヘッダセルを表すクラス。
	 * 
	 * @author s_nagai
	 * @since 2012/03/03
	 */
	private static class Header {

		/** クラス名 */
		String className;

		/** 階層構造を持つ変数名 */
		String[] valiableNameHierarchy;

		/**
		 * 引数に指定されたクラス名と変数名の配列を使用してヘッダオブジェクトを生成します。
		 * 
		 * @param className
		 *            クラス名
		 * @param valiableNameHierarchy
		 *            階層構造を持つ変数名
		 */
		Header(final String className, final String[] valiableNameHierarchy) {
			this.className = className;
			this.valiableNameHierarchy = valiableNameHierarchy;
		}
	}
}
