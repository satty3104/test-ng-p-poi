package test.tmp;

import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class ParameterCSVTest // extends BaseCSVTest
{
  @DataProvider
  public Object[][] csv() {
    return new Object[][] {
        new Object[] { new HashMap() }
    };
  }

       @Parameters({"fileName"})
       public ParameterCSVTest(String csvFile)
       {
         System.out.println("Ctor:" + csvFile);
       }

       @Test(dataProvider="csv")
       public void parameterBaseTest(Map<String, String> map)
       {
               System.out.println("Starting Test with data: " + map);
               Reporter.log(map.toString());
       }

       @Test(dataProvider="csv")
       public void parameterBaseTest2(Map<String, String> map)
       {
         System.out.println("Starting Test with data: " + map);
               Reporter.log(map.toString());
       }

       @Test
       public void noDataTest()
       {
         System.out.println("No data test");
               Reporter.log("No data test!");
       }
}