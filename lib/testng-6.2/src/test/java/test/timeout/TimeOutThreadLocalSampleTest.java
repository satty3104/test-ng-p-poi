package test.timeout;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TimeOutThreadLocalSampleTest {

  ThreadLocal<Integer> m_local = new ThreadLocal<Integer>() {
  };

  @BeforeMethod
  public void bm() {
    p("bm");
    m_local.set(42);
  }

  @Test(timeOut = 100000)
  public void f() {
    p("f");
    Assert.assertEquals(m_local.get(), (Integer) 42);
  }

  private void p(String string) {
    System.out.println("[TimeOutThreadLocalSampleTest] " + Thread.currentThread().getId()
        + " " + string);
  }
}
