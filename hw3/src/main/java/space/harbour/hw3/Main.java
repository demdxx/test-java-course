package space.harbour.hw3;

import java.util.Map;

import space.harbour.imap.ListMap;

public class Main {
  private final static int ELEMENT_COUNT = 100;

  public static void main(String[] args) {
    System.out.println("Run map test");
    Map<String, Integer> data = new ListMap<String, Integer>();
    for (var i = 0; i < ELEMENT_COUNT; i++) {
      data.put(String.format("item-%d", i), i);
    }

    for (Map.Entry<String, Integer> e : data.entrySet()) {
      System.out.println(e);
    }
  }
}
