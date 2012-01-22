package test.tmp.yuri;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class InvokedMethodListener implements IInvokedMethodListener {

  public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
    TestBase.setPassed();
  }

  public void afterInvocation(IInvokedMethod method, ITestResult result) {
    Reporter.setCurrentTestResult(result);
    if (method.isTestMethod()) {
      if (!TestBase.getPassed()) {
        result.setStatus(ITestResult.FAILURE);
      }
    }
  }
}
