package test.tmp.web;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BaseWeb {

  private boolean m_fail;

  public BaseWeb() {
  }

  public BaseWeb(boolean fail) {
    m_fail = fail;
  }

  @Test
  public void launchSite() {
    p("launchSite()");
  }

  @Test(dependsOnMethods = "launchSite")
  public void openRegisterPage() {
    p("openRegisterPage()");
    if (m_fail) {
      Assert.assertFalse(m_fail);
    }
  }

  @Test(dependsOnMethods = "openRegisterPage")
  public void enterRegistrationData() {
    p("enterRegistrationSite()");
  }

  private void p(String s) {
    System.out.println("[" + getClass() + "] " + s);
  }

  @Override
  public String toString() {
    return "[" + getClass() + " " + m_fail + "]";
  }
}

