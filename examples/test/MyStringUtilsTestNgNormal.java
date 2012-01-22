package test;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import main.MyStringUtils;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MyStringUtilsTestNgNormal {

	private MyStringUtils target;

	@BeforeClass
	public void setUp() {
		target = new MyStringUtils();
	}

	@Test(dataProvider = "createData")
	public void testConcat(String expect, String str1, String delim, String str2) {
		assertEquals(target.concat(str1, delim, str2), expect);
	}

	@DataProvider(name = "createData")
	public Iterator<Object[]> createData() {
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(new Object[] { "", null, null, null });
		data.add(new Object[] { "", "", null, null });
		data.add(new Object[] { "str1", "str1", null, null });
		data.add(new Object[] { "", null, "", null });
		data.add(new Object[] { "", "", "", null });
		data.add(new Object[] { "str1", "str1", "", null });
		data.add(new Object[] { "", null, ":", null });
		data.add(new Object[] { "", "", ":", null });
		data.add(new Object[] { "str1", "str1", ":", null });
		data.add(new Object[] { "", null, null, "" });
		data.add(new Object[] { "", "", null, "" });
		data.add(new Object[] { "str1", "str1", null, "" });
		data.add(new Object[] { "", null, "", "" });
		data.add(new Object[] { "", "", "", "" });
		data.add(new Object[] { "str1", "str1", "", "" });
		data.add(new Object[] { "", null, ":", "" });
		data.add(new Object[] { "", "", ":", "" });
		data.add(new Object[] { "str1", "str1", ":", "" });
		data.add(new Object[] { "str2", null, null, "str2" });
		data.add(new Object[] { "str2", "", null, "str2" });
		data.add(new Object[] { "str1str2", "str1", null, "str2" });
		data.add(new Object[] { "str2", null, "", "str2" });
		data.add(new Object[] { "str2", "", "", "str2" });
		data.add(new Object[] { "str1str2", "str1", "", "str2" });
		data.add(new Object[] { "str2", null, ":", "str2" });
		data.add(new Object[] { "str2", "", ":", "str2" });
		data.add(new Object[] { "str1:str2", "str1", ":", "str2" });
		return data.iterator();
	}
}
