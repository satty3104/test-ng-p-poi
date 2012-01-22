package test.tmp.felipe;

import org.testng.annotations.Test;

public class M {

  @Test(description="Do something", dependsOnMethods = { "test.tmp.felipe.OtherClass.method2test" })
  public void f() {
  }
}
