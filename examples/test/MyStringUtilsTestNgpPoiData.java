package test;

import java.util.Iterator;

import org.testng.annotations.DataProvider;

import s.n.testngppoi.iterator.HSSFDataProvider;

public class MyStringUtilsTestNgpPoiData {

	@DataProvider()
	public static Iterator<Object[]> testConcatData() {
		return new HSSFDataProvider("MyStringUtilsTestNgpPoiData.xls",
				"testConcatData");
	}

	@DataProvider()
	public static Iterator<Object[]> test() {
		return new HSSFDataProvider();
	}
}
