package s.n.testngppoi.factory.impl;

import java.util.Iterator;

import s.n.testngppoi.factory.SSFDataProviderFactory;

public abstract class AbstractSSFDataProviderFactory implements SSFDataProviderFactory {

	@Override
	public Iterator<Object[]> create() {
		return null;
	}

	@Override
	public Iterator<Object[]> create(String sheetName) {
		return null;
	}
}
