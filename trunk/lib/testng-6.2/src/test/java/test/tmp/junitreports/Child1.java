package test.tmp.junitreports;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Child1 extends Base {

  @BeforeMethod
  public void bm() {
//    throw new RuntimeException("Before method failing");
  }

  @Test
  public void child1() {}

  @Test
  public void child2() {}
}
