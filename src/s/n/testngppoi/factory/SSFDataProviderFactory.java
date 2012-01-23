package s.n.testngppoi.factory;

import java.util.Iterator;

public interface SSFDataProviderFactory {

	public Iterator<Object[]> create();

	public Iterator<Object[]> create(String fileName, String sheetName);

	public Iterator<Object[]> create(String filePath, String fileName,
			String sheetName);
}
