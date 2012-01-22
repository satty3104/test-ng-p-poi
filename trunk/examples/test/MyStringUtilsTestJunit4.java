package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import main.MyStringUtils;

public class MyStringUtilsTestJunit4 {

	private MyStringUtils target;

	@Before
	public void setUp() {
		target = new MyStringUtils();
	}

	@Test
	public void testConcat001() {
		assertEquals("", target.concat(null, null, null));
	}

	@Test
	public void testConcat002() {
		assertEquals("", target.concat("", null, null));
	}

	@Test
	public void testConcat003() {
		assertEquals("str1", target.concat("str1", null, null));
	}

	@Test
	public void testConcat004() {
		assertEquals("", target.concat(null, "", null));
	}

	@Test
	public void testConcat005() {
		assertEquals("", target.concat("", "", null));
	}

	@Test
	public void testConcat006() {
		assertEquals("str1", target.concat("str1", "", null));
	}

	@Test
	public void testConcat007() {
		assertEquals("", target.concat(null, ":", null));
	}

	@Test
	public void testConcat008() {
		assertEquals("", target.concat("", ":", null));
	}

	@Test
	public void testConcat009() {
		assertEquals("str1", target.concat("str1", ":", null));
	}

	@Test
	public void testConcat010() {
		assertEquals("", target.concat(null, null, ""));
	}

	@Test
	public void testConcat011() {
		assertEquals("", target.concat("", null, ""));
	}

	@Test
	public void testConcat012() {
		assertEquals("str1", target.concat("str1", null, ""));
	}

	@Test
	public void testConcat013() {
		assertEquals("", target.concat(null, "", ""));
	}

	@Test
	public void testConcat014() {
		assertEquals("", target.concat("", "", ""));
	}

	@Test
	public void testConcat015() {
		assertEquals("str1", target.concat("str1", "", ""));
	}

	@Test
	public void testConcat016() {
		assertEquals("", target.concat(null, ":", ""));
	}

	@Test
	public void testConcat017() {
		assertEquals("", target.concat("", ":", ""));
	}

	@Test
	public void testConcat018() {
		assertEquals("str1", target.concat("str1", ":", ""));
	}

	@Test
	public void testConcat019() {
		assertEquals("str2", target.concat(null, null, "str2"));
	}

	@Test
	public void testConcat020() {
		assertEquals("str2", target.concat("", null, "str2"));
	}

	@Test
	public void testConcat021() {
		assertEquals("str1str2", target.concat("str1", null, "str2"));
	}

	@Test
	public void testConcat022() {
		assertEquals("str2", target.concat(null, "", "str2"));
	}

	@Test
	public void testConcat023() {
		assertEquals("str2", target.concat("", "", "str2"));
	}

	@Test
	public void testConcat024() {
		assertEquals("str1str2", target.concat("str1", "", "str2"));
	}

	@Test
	public void testConcat025() {
		assertEquals("str2", target.concat(null, ":", "str2"));
	}

	@Test
	public void testConcat026() {
		assertEquals("str2", target.concat("", ":", "str2"));
	}

	@Test
	public void testConcat027() {
		assertEquals("str1:str2", target.concat("str1", ":", "str2"));
	}
}
