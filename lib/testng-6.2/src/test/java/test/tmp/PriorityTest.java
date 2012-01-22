package test.tmp;

import org.testng.annotations.Test;

public class PriorityTest {

  @Test(priority = 2)
  public void priorityTwo() {
    System.out.println(2);
  }

  @Test(dependsOnMethods = { "priorityTwo" })
  public void dependentA() {
    System.out.println("A");
  }

  @Test(dependsOnMethods = { "dependentA" })
  public void dependentB() {
    System.out.println("B");
  }

  @Test(priority = 3)
  public void priorityThree() {
    System.out.println(3);
  }

  @Test(dependsOnMethods = "priorityThree")
  public void dependsOnPriorityThree() {
    System.out.println("dependsOnPriorityThree");
  }
}