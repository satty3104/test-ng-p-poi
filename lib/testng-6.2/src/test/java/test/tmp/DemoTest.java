package test.tmp;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import test.tmp.DemoTest.L;

@Listeners(L.class)
public class DemoTest {

  public static class L implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
      System.out.println("After:" + Reporter.getCurrentTestResult());
    }
    
  }
  @BeforeMethod
  public void before() {
          System.out.println("before");
  }

  @Test
  public void nothing() {
          System.out.println("test");
  }

  @AfterMethod
  public void after() {
          System.out.println("after");
  }
}
