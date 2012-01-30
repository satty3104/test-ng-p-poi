package test;

import java.util.Iterator;

import org.testng.annotations.DataProvider;

import s.n.testngppoi.factory.SSFDataProviderFactory;
import s.n.testngppoi.factory.creator.SSFDataProviderFactoryCreator;
import s.n.testngppoi.type.Type;

public class MyStringUtilsTestNgpPoiData {

	@DataProvider()
	public static Iterator<Object[]> testConcatData() {
		return new SSFDataProviderFactoryCreator(Type.HSSF).getFactory(
				"MyStringUtilsTestNgpPoiData.xls").create("testConcatData");
	}

	@DataProvider()
	public static Iterator<Object[]> test() {
		SSFDataProviderFactory factory = new SSFDataProviderFactoryCreator(
				Type.XSSF).getFactory();
		return factory.create();
	}
}
