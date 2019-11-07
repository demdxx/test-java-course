package space.harbour.lesson3;

import java.lang.Iterable;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class MixIterator<E> implements Iterator<E> {
  protected int offset;
  protected List<E> data;
  public MixIterator(List<E> data) {
    this.offset = 0;
    this.data = data;
  }
  public boolean hasNext() { return this.data != null && this.offset < this.data.size(); }
  public E next() { return this.data.get(offset++); }
}
