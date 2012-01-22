package test.tmp.jai;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class Base {

    protected static String Selenium = "ALIVE";

    @BeforeSuite(description = "Perform class setup tasks")
    public void beforeClass() {
        System.out.println("@BeforeSuite");
    }

    @AfterSuite(description = "Perform class teardown tasks")
    public void afterClass() {
        System.out.println("@AfterSuite");
        Selenium = null;
    }
}
