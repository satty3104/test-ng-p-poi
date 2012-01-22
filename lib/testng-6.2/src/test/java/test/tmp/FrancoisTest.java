package test.tmp;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class FrancoisTest {
  private String m_text;
  private boolean m_flag;

  @Factory(dataProvider = "test1")
  public FrancoisTest(String text, boolean flag) {
    m_text = text;
    m_flag = flag;
  }

  @DataProvider(name = "test1")
  public static Object[][] createData1() {
    return new Object[][] { { "passed", true }, { "failed", false }, };
  }

  @Test
  public void m2() {
    System.out.println("m2:" + m_text + "," + m_flag);
    if (!m_flag) {
      throw new RuntimeException("horrible bug");
    }
  }

  @Test(dependsOnMethods = "m2")
  public void m3() {
    System.out.println("m3:" + m_text + "," + m_flag);
  }
}
