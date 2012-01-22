package test.tmp;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class ITestExample implements ITest {
  
  private String name;

  public ITestExample() {}

  public ITestExample(String name) {
      this.name = name;
  }

  public String getTestName() {
      return name;
  }

  @Factory
  public static ITestExample[] testInstances() {
      return new ITestExample[]{new ITestExample("instance 1"), new ITestExample("instance 2")};
  }

  @Test
  public void testPass() {
      Assert.assertTrue(true);
  }

//  @Test
  public void testFail() {
      Assert.fail();
  }
}