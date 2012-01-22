package test.tmp.jai;

import org.testng.annotations.Test;

public class SubB extends Base {

    private final String a;
    private final String b;

    public SubB(final String name, final String concat) {
        a = name;
        b = concat;
    }

    @Test
    public void test4() {
        System.out.println("test4");
        System.out.println(Selenium);
    }

    @Test(dependsOnMethods = {"test4"})
    public void test5() {
        System.out.println("test5");
        System.out.println(a);
    }

    @Test(dependsOnMethods = {"test4", "test5"})
    public void test6() {
        System.out.println("test6");
        System.out.println(b);
    }
}
