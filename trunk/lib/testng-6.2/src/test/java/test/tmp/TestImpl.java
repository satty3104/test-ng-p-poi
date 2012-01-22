package test.tmp;

import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class TestImpl{
  @Test
  public void test_1() throws Exception {
      Reporter.log("1 + Reporter: " + test);
  }


  public static String test = null;

  @BeforeSuite
  public static void initialization() throws Exception{
      test = "initialized";
  }
}