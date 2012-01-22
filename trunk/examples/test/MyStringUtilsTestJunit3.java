package test;

import junit.framework.TestCase;
import main.MyStringUtils;

public class MyStringUtilsTestJunit3 extends TestCase {

	private MyStringUtils target;

	@Override
	protected void setUp() throws Exception {
		target = new MyStringUtils();
	}

	public void testConcat001() {
		assertEquals("", target.concat(null, null, null));
	}

	public void testConcat002() {
		assertEquals("", target.concat("", null, null));
	}

	public void testConcat003() {
		assertEquals("str1", target.concat("str1", null, null));
	}

	public void testConcat004() {
		assertEquals("", target.concat(null, "", null));
	}

	public void testConcat005() {
		assertEquals("", target.concat("", "", null));
	}

	public void testConcat006() {
		assertEquals("str1", target.concat("str1", "", null));
	}

	public void testConcat007() {
		assertEquals("", target.concat(null, ":", null));
	}

	public void testConcat008() {
		assertEquals("", target.concat("", ":", null));
	}

	public void testConcat009() {
		assertEquals("str1", target.concat("str1", ":", null));
	}

	public void testConcat010() {
		assertEquals("", target.concat(null, null, ""));
	}

	public void testConcat011() {
		assertEquals("", target.concat("", null, ""));
	}

	public void testConcat012() {
		assertEquals("str1", target.concat("str1", null, ""));
	}

	public void testConcat013() {
		assertEquals("", target.concat(null, "", ""));
	}

	public void testConcat014() {
		assertEquals("", target.concat("", "", ""));
	}

	public void testConcat015() {
		assertEquals("str1", target.concat("str1", "", ""));
	}

	public void testConcat016() {
		assertEquals("", target.concat(null, ":", ""));
	}

	public void testConcat017() {
		assertEquals("", target.concat("", ":", ""));
	}

	public void testConcat018() {
		assertEquals("str1", target.concat("str1", ":", ""));
	}

	public void testConcat019() {
		assertEquals("str2", target.concat(null, null, "str2"));
	}

	public void testConcat020() {
		assertEquals("str2", target.concat("", null, "str2"));
	}

	public void testConcat021() {
		assertEquals("str1str2", target.concat("str1", null, "str2"));
	}

	public void testConcat022() {
		assertEquals("str2", target.concat(null, "", "str2"));
	}

	public void testConcat023() {
		assertEquals("str2", target.concat("", "", "str2"));
	}

	public void testConcat024() {
		assertEquals("str1str2", target.concat("str1", "", "str2"));
	}

	public void testConcat025() {
		assertEquals("str2", target.concat(null, ":", "str2"));
	}

	public void testConcat026() {
		assertEquals("str2", target.concat("", ":", "str2"));
	}

	public void testConcat027() {
		assertEquals("str1:str2", target.concat("str1", ":", "str2"));
	}
}
