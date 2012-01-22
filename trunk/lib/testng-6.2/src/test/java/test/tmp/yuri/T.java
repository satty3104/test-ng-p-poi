package test.tmp.yuri;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;

import junit.framework.Assert;

import test.SimpleBaseTest;

public class T extends SimpleBaseTest {

  @Test
  public void f() {
    TestNG tng = create(TestSampleImpl.class);
    TestListenerAdapter tla = new TestListenerAdapter();
    tng.addListener(tla);
    tng.setVerbose(10);
    tng.run();
    Assert.assertEquals(tla.getPassedTests().size(), 0);
    Assert.assertEquals(tla.getFailedTests().size(), 1);
  }
}
