package test;

import static org.testng.Assert.assertEquals;

import java.util.Map;

import main.MyStringUtils;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import s.n.testngppoi.support.ExcelTestNgSupport;

public class MyStringUtilsTestNgpPoi extends ExcelTestNgSupport {

	private MyStringUtils target;

	@BeforeClass
	public void setUp() {
		target = new MyStringUtils();
	}

	@Test(dataProviderClass = MyStringUtilsTestNgpPoiData.class, dataProvider = "testConcatData")
	public void testConcat(Map<String, Object> map) {
		initMethod(map);
		String str1 = getString("str1");
		String delim = getString("delim");
		String str2 = getString("str2");
		String expect = getString("expect");
		assertEquals(target.concat(str1, delim, str2), expect);
	}

	@Test(dataProviderClass = MyStringUtilsTestNgpPoiData.class, dataProvider = "test")
	public void test(Map<String, Object> map) {
		initMethod(map);
		// String str1 = getString("str1");
		// String delim = getString("delim");
		// String str2 = getString("str2");
		// String expect = getString("expect");
		// assertEquals(target.concat(str1, delim, str2), expect);

		Object o = get("hoge");
//		System.out.println(o == null ? null : o);
	}
}
