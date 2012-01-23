package s.n.testngppoi.factory.impl;

import s.n.testngppoi.type.Type;

public class HSSFDataProviderFactory extends AbstractSSFDataProviderFactory {

	@Override
	protected String getExtension() {
		return Type.HSSF.getExtension();
	}
}
