package test;

import java.util.Iterator;

import org.testng.annotations.DataProvider;

import s.n.testngppoi.factory.creator.SSFDataProviderFactoryCreator;
import s.n.testngppoi.type.Type;

public class MyStringUtilsTestNgpPoiData {

	@DataProvider()
	public static Iterator<Object[]> testConcatData() {
		return SSFDataProviderFactoryCreator.getInstance(Type.HSSF).create(
				"MyStringUtilsTestNgpPoiData.xls", "testConcatData");
	}

	@DataProvider()
	public static Iterator<Object[]> test() {
		return SSFDataProviderFactoryCreator.getInstance(Type.HSSF).create();
	}
}
