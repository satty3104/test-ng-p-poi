package test.tmp;

import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.TestListenerAdapter;

public class TLA extends TestListenerAdapter {
  @Override
  public void onTestStart(ITestResult result) {
    throw new SkipException("Skipped");
  }
}

