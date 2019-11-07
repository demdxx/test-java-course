package space.harbour.lesson3;

import java.util.List;
import java.util.Arrays;

import space.harbour.lesson3.MixIterator;

public class Main {
  protected static class Integers {
    public static <T extends Number> T largest(T[] arr) {
      T l = null;
      if (arr == null)
        return null;
      for (int i = 0; i < arr.length; i++) {
        if (l == null || l.doubleValue() < arr[i].doubleValue())
          l = arr[i];
      }
      return l;
    }
  }

  public static void main(String[] args) {
    System.out.printf("largest element 1: %s\n", Integers.largest(new Integer[] { 1, 10, 23, 123, 3 }));
    System.out.printf("largest element 2: %s\n", Integers.largest(new Double[] { 1.1, 10.0, 23.7, 123.12, 3.0 }));
    System.out.printf("largest element 3: %s\n", Integers.largest(new Number[] { 1, 1.10, (short)(22), (long)(123), 3 }));
    System.out.printf("largest element null: %s\n", Integers.largest(null));

    List<Integer> ll = Arrays.asList(Integer.valueOf(1), 2);
    MixIterable<Integer> list = new MixIterable<Integer>(ll);
    for (Integer it : list) {
      System.out.printf("-> %s\n", it);
    }
  }
}