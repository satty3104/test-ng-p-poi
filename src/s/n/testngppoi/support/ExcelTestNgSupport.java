package s.n.testngppoi.support;

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
		return castObject(map.get(key));
	}

	public int getInt(String key) {
		Double d = castObject(map.get(key));
		return d.intValue();
	}

	public double getDouble(String key) {
		return castObject(map.get(key));
	}

	public boolean getBoolean(String key) {
		return castObject(map.get(key));
	}
}
