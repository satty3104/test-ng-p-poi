package test.tmp;

import org.testng.annotations.Factory;

public class AFactory {
  @Factory(enabled = false)
  public Object[] create() {
    return new Object[] { new A(), new A() };
  }

}
