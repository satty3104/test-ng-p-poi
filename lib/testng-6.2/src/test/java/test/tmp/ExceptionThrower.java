package test.tmp;

import org.testng.annotations.Test;

@Test
public class ExceptionThrower {
  @Test
  public void initTesting() {
    System.out.println("expect this");
    throw new RuntimeException();
  }

  // ///////////////
  @Test(dependsOnMethods = "initTesting")
  public void dummyTest1() {
    System.out.println("should fail to get here dummyTest1");
  }

  // ///////////////
  @Test(dependsOnMethods = "dummyTest1")
  public void dummyTest2() {
    System.out.println("should fail to get here dummyTest2");
  }

  // ///////////////
  @Test(dependsOnMethods = "dummyTest2")
  public void dummyTest3() {
    System.out.println("should fail to get here dummyTest3");
  }

  // ///////////////
  @Test(dependsOnMethods = "dummyTest3")
  public void dummyTest4() {
    System.out.println("should fail to get here dummyTest4");
  }

  // ///////////////
  @Test(dependsOnMethods = "dummyTest4")
  public void dummyTest5() {
    System.out.println("should fail to get here dummyTest5");
  }
}