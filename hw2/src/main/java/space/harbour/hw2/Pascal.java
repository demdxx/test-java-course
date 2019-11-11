package space.harbour.hw2;

public class Pascal {
  public static int numberForCell(int x, int y) {
    if (y == 0 || y == x)
      return 1;
    return numberForCell(x - 1, y - 1) + numberForCell(x - 1, y);
  }

  public static String toString(int size) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < i + 1; j++) {
        builder.append(numberForCell(i, j));
        if (j == i) {
          if (j != size-1) {
            builder.append('\n');
          }
        } else {
          builder.append(' ');
        }
      }
    }
    return builder.toString();
  }
}