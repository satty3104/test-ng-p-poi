package test.tmp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class ParallelTest {
  @DataProvider(name = "Scenario")
  public Iterator<Object[]> provideScenarioData() {
    int count = 10;
    List<Object[]> index = new ArrayList<Object[]>();
    for (int i = 0; i < count; i++) {
      Object[] obj = new Object[1];
      obj[0] = i;
      index.add(obj);
    }
    return index.iterator();
  }

  /**
   * Generate 3 test in this factory
   * 
   * @return
   */
  @Factory(dataProvider = "Scenario")
  public static Object[] createInstances(int index) {
    Object[] result = new Object[1];
    result[0] = new ParallelTest(index);
    return result;
  }

  private final int instanceIndex;

  public ParallelTest() {
    this.instanceIndex = -1;
  }

  public ParallelTest(int i) {
    this.instanceIndex = i;
  }

  @BeforeTest()
  public void before() {
    System.out.println(String.format("Before Test %s, threadID: %s", this.instanceIndex, Thread
        .currentThread().getId()));
  }

  @BeforeClass()
  public void beforeClass() {
    System.out.println(String.format("Before Class %s, threadID: %s", this.instanceIndex, Thread
        .currentThread().getId()));
  }

  @Test(description = "Doing the test 1.")
  public void timeOutTest1() {
    System.out.println(String.format("Test %s.%s threadID: %s", this.instanceIndex, 1, Thread
        .currentThread().getId()));
  }

  @Test(description = "Doing the test 2.")
  public void timeOutTest2() {
    System.out.println(String.format("Test %s.%s threadID: %s", this.instanceIndex, 2, Thread
        .currentThread().getId()));
  }

  @AfterClass(alwaysRun = true)
  public void afterClass() {
    System.out.println(String.format("After class %s, threadID: %s", this.instanceIndex, Thread
        .currentThread().getId()));
  }

  @AfterTest(alwaysRun = true)
  public void after() {
    System.out.println(String.format("After Test %s, threadID: %s", this.instanceIndex, Thread
        .currentThread().getId()));
  }
}