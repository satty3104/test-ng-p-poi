package test.tmp;

import org.testng.annotations.Test;

@Test
public class BeforeMethodTest extends AbstractBeforeMethodTest {

 @Test(groups = "fastTest")
 public void test_commonResource() {
   System.out.println("BeforeMethodTest - test_commonResource length=" + commonResource.length());
 }

 @Test(groups = "noFastTest")
 public void test_groupFiltering() {
   System.out.println("BeforeMethodTest - test_groupFiltering");
 }
}