package s.n.testngppoi.factory.impl;

import s.n.testngppoi.type.Type;

public class XSSFDataProviderFactory extends AbstractSSFDataProviderFactory {

	@Override
	protected String getExtension() {
		return Type.XSSF.getExtension();
	}
}
