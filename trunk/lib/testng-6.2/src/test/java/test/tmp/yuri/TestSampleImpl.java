package test.tmp.yuri;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(InvokedMethodListener.class)
public class TestSampleImpl extends TestBase {
  @Test
  public void test_1() throws Exception {
    TestBase.setFailed();
  }
}