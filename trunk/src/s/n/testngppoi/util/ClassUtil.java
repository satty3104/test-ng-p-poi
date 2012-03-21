package s.n.testngppoi.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import s.n.testngppoi.exception.ClassUtilException;

// TODO 各メソッドにNULLチェックを入れる
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ClassUtil {

	private static final Map<String, Class> PRIMITIVE_CLASS;

	static {
		final Map<String, Class> primitiveClass = new HashMap<String, Class>() {
			private static final long serialVersionUID = -5552461840762896526L;
			{
				put("byte", byte.class);
				put("short", short.class);
				put("int", int.class);
				put("long", long.class);
				put("float", float.class);
				put("double", double.class);
				put("char", char.class);
				put("boolean", boolean.class);
			}
		};
		PRIMITIVE_CLASS = Collections.unmodifiableMap(primitiveClass);
	}

	public static Class getClass(final String className)
			throws ClassUtilException {
		final Class c = getPrimitiveClass(className);
		if (c != null) {
			return c;
		}
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO
			throw new ClassUtilException("");
		}
	}

	private static Class getPrimitiveClass(final String className) {
		return PRIMITIVE_CLASS.get(className);
	}

	public static Object createInstance(Class c) throws ClassUtilException {
		try {
			return c.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		}
	}

	public static Constructor getAccessibleDeclaredConstructor(Class c,
			Class[] types) throws ClassUtilException {
		Constructor con = null;
		try {
			con = c.getDeclaredConstructor(types);
			con.setAccessible(true);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		}
		return con;
	}

	public static Object createInstanceFromConstructor(Constructor con,
			Object[] args) throws ClassUtilException {
		try {
			return con.newInstance(args);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		}
	}

	public static Field getAccessibleDeclaredField(String fieldName, Object o)
			throws ClassUtilException {
		Field f = null;
		try {
			f = o.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		}
		return f;
	}

	public static void setValueToField(Object targetObj, Field targetField,
			Object value) throws ClassUtilException {
		try {
			targetField.set(targetObj, value);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		}
	}

	public static Object getObject(Object o, Field f) throws ClassUtilException {
		try {
			return f.get(o);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new ClassUtilException("");
		}
	}
}