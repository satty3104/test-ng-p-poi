package test.tmp;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import test.tmp.A.TL;

//@Test(sequential = true)
@Listeners(TL.class)
public class A {

  public static class TL implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
    }

    @Override
    public void onTestSuccess(ITestResult result) {
    }

    @Override
    public void onTestFailure(ITestResult result) {
    }

    @Override
    public void onTestSkipped(ITestResult result) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onStart(ITestContext context) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void onFinish(ITestContext context) {
      // TODO Auto-generated method stub
    }

  }
  
  @BeforeMethod
  public void bm() throws InterruptedException {
    Thread.sleep(1000);
//    throw new RuntimeException("Ex");
//    System.out.println("@BeforeMethod");
  }

  @BeforeMethod
  public void bm2() throws InterruptedException {
    Thread.sleep(500);
  }

  @AfterMethod
  public void am() throws InterruptedException {
    Thread.sleep(2000);
  }
  @Test
  public void a1() {
//    throw new RuntimeException();
//    System.out.println("Context:" + foo); // context.getSuite().getXmlSuite().getFileName());
    Assert.assertTrue(true);
//    System.out.println("test1");
  }

  @Test(dependsOnMethods = "a1")
  public void a2() {
//    System.out.println("test2");
//    throw new RuntimeException("We have a problem");
//    System.out.println("a2 " + Thread.currentThread().getId());
  }

  @Test(enabled = false, description = "This test is disabled")
  public void a3() {
//    throw new SkipException("Skipped");
  }
}

