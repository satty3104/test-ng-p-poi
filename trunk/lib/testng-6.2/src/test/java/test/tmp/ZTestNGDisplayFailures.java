package test.tmp;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(enabled=true)
public class ZTestNGDisplayFailures{
    
    @Test(dataProvider="fake",
            description = "Fails half the TCs, based on data from provider")
    public void testFailDisplayFromDataProvider(Integer cnt) throws InterruptedException{
        if (cnt % 2 == 0){
            Assert.fail("Failing even numbers");
            Thread.sleep(5000);
        }
    }
    
    @DataProvider(name="fake", parallel = true)
    public static Object[][] dataProvider(){
        Integer[] arg = {2,1};
        Object[][] qss = new Object[arg.length][1];
        int cnt = 0;
        for(int i = 0; i < arg.length; i++){
            Object[] args = new Object[1];
            args[0] = arg[i];
            qss[cnt] = args;
            cnt++;
        }
        return qss;

    }
}