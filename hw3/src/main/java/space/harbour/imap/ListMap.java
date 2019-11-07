// Task description
//
// 0) Use project template archive from the previous assignment. Don't forget to update project name in pom.xml
//
// 1) Implement your own MyHashMap<K, V> implements Map<K, V>. To do that you will also
//    need to implement a class of pairs Element<K, V>. Use array for buckets and LinkedList<Element> to handle collisions.
//
// 2) Let me know if you are having difficulties implementing your own hash function.
//
// 3) Submit a link to a successful build in Travis CI.

package space.harbour.imap;

import java.util.*;

// ListMap implementation of close-addressing map interface
// @NOTE Map interface uses Set<> for storing of keys but the task requires to implement
// the custom hashSet, so it makes using of Map<> interface redundant and even more difficult
// ! Not thread-safe implementation
public class ListMap<K extends Comparable<K>, V> implements Map<K, V> {

  static class Node<K, V> implements Map.Entry<K, V> {
    final K key;
    V value;

    Node(K key, V value) {
      this.key = key;
      this.value = value;
    }

    public final K getKey() {
      return key;
    }

    public final V getValue() {
      return value;
    }

    public final String toString() {
      return key + "=" + value;
    }

    public final int hashCode() {
      return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    public final V setValue(V newValue) {
      V oldValue = value;
      value = newValue;
      return oldValue;
    }

    public final boolean equals(Object o) {
      if (o == this)
        return true;
      if (o instanceof Map.Entry) {
        Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
        if (Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue()))
          return true;
      }
      return false;
    }
  }

  // Minamal backet block size
  final static int BLOCK_SIZE = 128;

  // Backet storage
  private List<Map.Entry<K, V>>[] data;

  // Cache of data as set of entries
  private Set<Map.Entry<K, V>> dataCache;

  // Count of lements in the map
  private int size;

  public ListMap() {
    data = null;
  }

  public ListMap(Map.Entry<K, V>... params) throws Exception {
    data = null;
    for (var p : params) {
      put(p);
    }
  }

  public int size() {
    return this.size;
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Returns {@code true} if this map contains a mapping for the specified key.
   */
  public boolean containsKey(Object key) {
    var i = iterator();
    while (i.hasNext()) {
      var itKey = i.next().getKey();
      if ((key == null && itKey == null) || key.equals(itKey))
        return true;
    }
    return false;
  }

  /**
   * Returns {@code true} if this map maps one or more keys to the specified
   * value.
   */
  public boolean containsValue(Object value) {
    var i = iterator();
    while (i.hasNext()) {
      var itVal = i.next().getValue();
      if ((value == null && itVal == null) || value.equals(itVal))
        return true;
    }
    return false;
  }

  public Map.Entry<K, V> getEntry(Object key) {
    var i = iterator();
    while (i.hasNext()) {
      var it = i.next();
      var itKey = it.getKey();
      if ((key == null && itKey == null) || key.equals(itKey))
        return it;
    }
    return null;
  }

  /**
   * Returns the value to which the specified key is mapped, or {@code null} if
   * this map contains no mapping for the key.
   */
  public V get(Object key) {
    var item = getEntry(key);
    return item == null ? null : item.getValue();
  }

  public Iterator<Map.Entry<K, V>> iterator() {
    return new MapIterator<>(data);
  }

  /// Modification Operations

  /**
   * Associates the specified value with the specified key in this map (optional
   * operation).
   *
   * @param key   key with which the specified value is to be associated
   * @param value value to be associated with the specified key
   * @return the previous value associated with {@code key}, or {@code null} if
   *         there was no mapping for {@code key}. (A {@code null} return can also
   *         indicate that the map previously associated {@code null} with
   *         {@code key}, if the implementation supports {@code null} values.)
   */
  public V put(K key, V value) {
    return put(new Node<>(key, value));
  }

  public V put(Map.Entry<K, V> entry) {
    var item = getEntry(entry.getKey());
    if (item != null) {
      // Rewrite exists value
      return item.setValue(entry.getValue());
    }

    if (data == null) {
      data = new List[BLOCK_SIZE];
    }

    var pageCount = data.length / BLOCK_SIZE + (data.length % BLOCK_SIZE > 0 ? 1 : 0);
    var pageOcupated = size() / BLOCK_SIZE + (size() % BLOCK_SIZE > 0 ? 1 : 0);

    // grow and rebuild map if necessary
    if (pageCount < pageOcupated) {
      var newCount = 0;
      var newSize = pageOcupated * BLOCK_SIZE;
      List<Map.Entry<K, V>>[] newData = new List[newSize];
      // Copy all old objects into the new data store
      var iter = iterator();
      while (iter.hasNext()) {
        var nextItem = iter.next();
        var position = Math.abs(nextItem.hashCode() % newSize);
        if (newData[position] == null) {
          newData[position] = new ArrayList<Map.Entry<K, V>>();
        }
        if (!newData[position].add(nextItem)) {
          throw new Error("invalid resize of backets");
        }
        newCount++;
      }
      // Replace current data store
      data = newData;
      size = newCount;
    }

    // Insert the new item
    var position = Math.abs(entry.hashCode() % data.length);
    if (data[position] == null) {
      data[position] = new ArrayList<Map.Entry<K, V>>();
    }
    if (!data[position].add(entry)) {
      throw new Error("invalid add element");
    }
    size++;

    clearCache();
    return null;
  }

  /**
   * Removes the mapping for a key from this map if it is present (optional
   * operation).
   */
  public V remove(Object key) {
    for (int i = 0; i < data.length; i++) {
      if (data[i] != null) {
        for (var p : data[i]) {
          if (key.equals(p.getKey())) {
            clearCache();
            if (!data[i].remove(p)) {
              throw new Error("can't remove item from map");
            }
            size--;
            return p.getValue();
          }
        }
      }
    }
    return null;
  }

  /// Bulk Operations

  /**
   * Copies all of the mappings from the specified map to this map
   */
  public void putAll(Map<? extends K, ? extends V> m) {
    for (var p : m.entrySet()) {
      if (put(new Node<>(p.getKey(), p.getValue())) == null) {
        throw new Error("can't add all elements into the map");
      }
    }
  }

  /**
   * Remove cached items as Set of items
   */
  protected void clearCache() {
    if (dataCache != null) {
      dataCache.clear();
    }
  }

  /**
   * Removes all of the mappings from this map (optional operation). The map will
   * be empty after this call returns.
   */
  public void clear() {
    clearCache();
    size = 0;
    if (data != null) {
      for (int i = 0; i < data.length; i++) {
        if (data[i] != null) {
          data[i].clear();
        }
      }
      data = null;
    }
  }

  /// Views

  public Set<K> keySet() {
    Set<K> keys = new HashSet<K>();
    forEach((k, v) -> {
      keys.add(k);
    });
    return keys;
  }

  public Collection<V> values() {
    List<V> list = new ArrayList<V>(size);
    forEach((k, v) -> {
      list.add(v);
    });
    return list;
  }

  /**
   * Returns a {@link Set} view of the mappings contained in this map.
   * 
   * @throws Error if it cant allocate etry set
   */
  public Set<Map.Entry<K, V>> entrySet() {
    if (dataCache != null && size() == dataCache.size())
      return dataCache;
    if (dataCache == null)
      dataCache = new HashSet<Map.Entry<K, V>>();
    else
      dataCache.clear();

    var iter = iterator();
    while (iter.hasNext()) {
      if (!dataCache.add(iter.next()))
        throw new Error("can't prepare entry set");
    }
    return dataCache;
  }
}
