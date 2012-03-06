package s.n.testngppoi.support;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public abstract class ExcelTestNgSupport {

	protected Map<String, Object> map;

	public void initMethod(Map<String, Object> map) {
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	public <T> T castObject(Object o) {
		return (T) o;
	}

	public String getString(String key) {
		return castObject(get(key));
	}

	public int getInt(String key) {
		Double d = castObject(get(key));
		return d.intValue();
	}

	public double getDouble(String key) {
		return castObject(get(key));
	}

	public boolean getBoolean(String key) {
		return castObject(get(key));
	}

	public Calendar getCalendar(String key) {
		Date d = castObject(get(key));
		if (d == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c;
	}

	public Object get(String key) {
		return map.get(key);
	}
}
