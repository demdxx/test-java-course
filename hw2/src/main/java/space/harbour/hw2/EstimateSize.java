package space.harbour.hw2;

import java.util.ArrayList;
import java.util.List;

public class EstimateSize {
  public interface Func {
    Object op();
  }

  static List<Object> objects;

  public static long filled() {
    return Runtime.getRuntime().totalMemory()
      - Runtime.getRuntime().freeMemory();
  }

  public static int size(Func func) {
    objects = new ArrayList<>();
    var start = filled();
    Object ref = func.op();
    objects.add(ref);
    var size = (int)(filled() - start);
    if (ref != null) { ref = null; }
    return size;
  }

  public static int intSize() {
    return size(() -> { return 1; });
  }

  public static int refSize() {
    return size(() -> { return null; });
  }

  public static int objSize() {
    return size(() -> { return new Object(); });
  }

  public static int stringSize() {
    return size(() -> { return new String("test"); });
  }
}