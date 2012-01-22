package test.tmp;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

@Test
public class RoundRobinTest {
  protected Integer num1, num2;

  @Factory(dataProvider = "mydata")
  public RoundRobinTest(Integer num1, Integer num2) {
    this.num1 = num1;
    this.num2 = num2;
  }

  @Test
  public void positive_Test() {
    for (Integer num : new Integer[] { num1, num2 }) {
      if (num < 0)
        throw new AssertionError("Number is not positive! " + num);
    }
  }

  @Test(dependsOnMethods = "positive_Test")
  public void even_Test() {
    for (Integer num : new Integer[] { num1, num2 }) {
      if (num % 2 != 0)
        throw new AssertionError("Number is not even! " + num);
    }
  }

  @Test(dependsOnMethods = "even_Test")
  public void mult3_Test() {
    for (Integer num : new Integer[] { num1, num2 }) {
      if (num % 3 != 0)
        throw new AssertionError("Number is not a multiple of 3! " + num);
    }
  }

  @DataProvider(name = "mydata")
  public static Object[][] getData() {
    return new Object[][] { { 24, 36 }, { 6, 8 }, { 2, 4 }, { -1, 13 }, { -7, 6 }, { 12, 1200 } };
  }

}