package space.harbour.lesson2;

import java.lang.OutOfMemoryError;

public class OutOfMemory {
  protected static int[] allocate(int v) {
    return new int[v];
  }

  public static void main(String[] args) {
    System.out.println(String.format("Out of memory: %d", Integer.SIZE/8));

    int allocated = 0;
    int max = 64 * 1024 * 1024;
    for (int i = 10_000_000;;i+=1_000) {
      try {
        allocate(i);
        allocated = i;
      } catch (OutOfMemoryError e) {
        break;
      }
    }
    System.out.println(String.format("Allocated: %d - INT SIZE %d", allocated, max/allocated));
  }
}