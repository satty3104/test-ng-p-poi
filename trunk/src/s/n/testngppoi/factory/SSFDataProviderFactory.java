package s.n.testngppoi.factory;

import java.util.Iterator;

public interface SSFDataProviderFactory {

	public Iterator<Object[]> create();

	public Iterator<Object[]> create(String sheetName);
}
