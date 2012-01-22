package test.tmp.yuri;

public class TestBase {
  private static boolean passed = true;

  public static void setFailed() {
    passed = false;
  }

  public static void setPassed() {
    passed = true;
  }

  public static boolean getPassed() {
    return passed;
  }
}