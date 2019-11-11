package space.harbour.hw2;

import org.junit.Assert;
import org.junit.Test;

public class PascalTests {
  @Test
  public void triangleTests() {
    int[] sizes = new int[]{1, 2, 3, 4, 5};
    String[] ansvers = {
      "1",
      "1\n1 1",
      "1\n1 1\n1 2 1",
      "1\n1 1\n1 2 1\n1 3 3 1",
      "1\n1 1\n1 2 1\n1 3 3 1\n1 4 6 4 1",
    };
    for (int i = 0; i < sizes.length; i++) {
      String s = Pascal.toString(sizes[i]);
      Assert.assertEquals("Invalid pascal conversion", ansvers[i], s);
    }
  }
}
