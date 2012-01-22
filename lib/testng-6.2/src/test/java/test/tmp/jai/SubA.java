package test.tmp.jai;

import org.testng.annotations.Test;

public class SubA extends Base {

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
        System.out.println(Selenium);
    }

    @Test
    public void test3() {
        System.out.println("test3");
    }
}
