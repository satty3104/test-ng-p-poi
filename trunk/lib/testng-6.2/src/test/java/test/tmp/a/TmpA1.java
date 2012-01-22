package test.tmp.a;

import org.testng.annotations.Test;

public class TmpA1 {
  @Test(groups = "group-a")
  public void tmpa11() {
    System.out.println("tmpa11");
  }

  @Test
  public void tmpa12() {
    System.out.println("tmpa12");
  }
}