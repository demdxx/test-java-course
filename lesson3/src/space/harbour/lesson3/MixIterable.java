package space.harbour.lesson3;

import java.lang.Iterable;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class MixIterable<T> implements Iterable<T> {
  protected List<T> data;

  MixIterable(List<T> list) {
    this.data = list;
    if (this.data == null)
      this.data = new ArrayList<T>();
  }

  public Iterator<T> iterator() {
    return new MixIterator<T>(this.data);
  }

  boolean add(T value) {
    return data.add(value);
  }
}