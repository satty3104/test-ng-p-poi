package s.n.testngppoi.dataprovider.delegate;

import java.util.Map;

import org.apache.poi.ss.usermodel.Row;

public interface RowDelegate {

	public Map<String, Object> getMap(Row row);

	public void addRowNum();

	public int getRowNum();
}
