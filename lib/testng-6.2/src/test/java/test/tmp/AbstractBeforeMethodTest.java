package test.tmp;

import org.testng.annotations.BeforeMethod;

public class AbstractBeforeMethodTest {
  String commonResource;

  @BeforeMethod
  public void commonSetUp() {
    commonResource = "myCommonResource";
  }
}
