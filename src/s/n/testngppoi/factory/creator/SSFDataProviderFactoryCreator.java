package s.n.testngppoi.factory.creator;

import s.n.testngppoi.factory.SSFDataProviderFactory;
import s.n.testngppoi.factory.impl.HSSFDataProviderFactory;
import s.n.testngppoi.factory.impl.XSSFDataProviderFactory;
import s.n.testngppoi.type.Type;

public class SSFDataProviderFactoryCreator {

	public static SSFDataProviderFactory getInstance(Type type) {
		if (type == null) {
			throw new NullPointerException("The argument type must not be null.");
		}
		switch (type) {
			case HSSF:
				return new HSSFDataProviderFactory();
			case XSSF:
				return new XSSFDataProviderFactory();
			default:
				throw new IllegalArgumentException("The argument type is illegal. [" + type.toString() + "]");
		}
	}
}
