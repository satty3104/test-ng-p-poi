package test.tmp;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class InstanceFailureSampleTest {

  private int m_n;

  @Factory(dataProvider = "dp")
  public InstanceFailureSampleTest(int n) {
    m_n = n;
  }

  @DataProvider
  static public Object[][] dp() {
    return new Object[][] {
        { 0 },
        { 1 },
        { 2 },
        { 3 },
    };
  }

  @Test
  public void a() {
    p("a");
    if (m_n % 2 == 0) throw new RuntimeException();
  }

  @Test(dependsOnMethods = "a")
  public void b() {
    p("b");
  }

  @Test(dependsOnMethods = "b")
  public void c() {
    p("c");
  }

  private void p(String string) {
    System.out.println(string + ":" + m_n);
  }

  @Override
  public String toString() {
    return "[n:" + m_n + "]";
  }
}
