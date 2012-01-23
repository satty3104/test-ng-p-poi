package test;

import java.util.Iterator;

import org.testng.annotations.DataProvider;

import s.n.testngppoi.factory.SSFDataProviderFactory;
import s.n.testngppoi.factory.creator.SSFDataProviderFactoryCreator;
import s.n.testngppoi.iterator.HSSFDataProvider;
import s.n.testngppoi.type.Type;

public class MyStringUtilsTestNgpPoiData {

	@DataProvider()
	public static Iterator<Object[]> testConcatData() {
		return new HSSFDataProvider("MyStringUtilsTestNgpPoiData.xls", "testConcatData");
	}

	@DataProvider()
	public static Iterator<Object[]> test() {
		SSFDataProviderFactory factory = SSFDataProviderFactoryCreator.getInstance(Type.HSSF);
		return factory.create();
	}
}
