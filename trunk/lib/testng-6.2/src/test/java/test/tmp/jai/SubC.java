package test.tmp.jai;

import org.testng.annotations.Test;

public class SubC extends Base {

    @Test
    public void test7() {
        System.out.println("test7");
        Selenium = "DEAD";
    }

    @Test
    public void test8() {
        System.out.println("test8");
        System.out.println(Selenium);
    }

    @Test
    public void test9() {
        System.out.println("test9");

    }
}
