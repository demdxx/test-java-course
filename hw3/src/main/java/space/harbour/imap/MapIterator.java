package space.harbour.imap;

import java.util.Iterator;
import java.util.List;

public class MapIterator<E> implements Iterator<E> {
  // Offset of the backet item
  protected int offset;

  // Offset inside the backet elements in collision
  protected int elementOffset;

  // Backet storage
  protected List<E>[] data;

  public MapIterator(List<E>[] data) {
    this.offset = 0;
    this.elementOffset = 0;
    this.data = data;
  }

  public boolean hasNext() {
    if (data == null || offset >= data.length)
      return false;
    var _elementOffset = elementOffset;
    for (var i = offset; i < data.length; i++) {
      if (data[i] == null) {
        _elementOffset = 0;
        continue;
      }
      if (_elementOffset < data[i].size()) {
        return true;
      }
    }
    return false;
  }

  public E next() {
    if (data == null || offset >= data.length)
      return null;
    for (; offset < data.length; offset++) {
      if (data[offset] != null && elementOffset < data[offset].size()) {
        return data[offset].get(elementOffset++);
      }
      elementOffset = 0;
    }
    return null;
  }
}
