package test.tmp;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ExampleTest {

  private String exampleString = null;

  @BeforeSuite
  public void doBeforeSuite() {
     exampleString = "beforeSuite";
  }

  @BeforeTest
  public void doBeforeTest() {
     exampleString = "beforeTest";
  }

  @BeforeGroups
  public void doBeforeGroups() {
     exampleString = "BeforeGroups";
  }

  @BeforeClass
  public void doBeforeClass() {
     exampleString = "BeforeClass";
  }

  @BeforeMethod(alwaysRun = true)
  public void doBeforeMethod() {
     exampleString = "BeforeMethod";
  }

  @Test(groups="before")
  public void doTest() {
     Assert.assertNotNull(exampleString);
  }
}