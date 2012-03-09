package s.n.testngppoi.factory;

import java.util.Iterator;

public interface DataProviderFactory {

	public Iterator<Object[]> create();
}
