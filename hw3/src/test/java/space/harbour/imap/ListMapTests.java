package space.harbour.imap;

import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import space.harbour.imap.ListMap;

public class ListMapTests {
  static final int MAX_ELEMENTS = 1000;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  public ListMap<String, Integer> newMap() {
    var data = new ListMap<String, Integer>();
    for (int i = 0; i < MAX_ELEMENTS; i++) {
      data.put(String.format("test-%d", i), i);
    }
    return data;
  }

  @Test
  public void dataClearTests() {
    var data = newMap();
    data.clear();
    Assert.assertEquals("Size incorrect", 0, data.size());
  }

  @Test
  public void insertTests() {
    var data = newMap();
    Assert.assertEquals("Size incorrect", MAX_ELEMENTS, data.size());

    for (int i = 0; i < MAX_ELEMENTS; i++) {
      var key = String.format("test-%d", i);
      Assert.assertTrue("can find: "+key, data.containsKey(key));
    }
    data.clear();
  }

  @Test
  public void removeTest() {
    var data = newMap();

    for (int i = 0; i < MAX_ELEMENTS/10; i++) {
      var key = String.format("test-%d", i);
      Assert.assertTrue("can remove: "+key, data.remove(key) != null);
    }

    for (int i = 0; i < MAX_ELEMENTS; i++) {
      var key = String.format("test-%d", i);
      if (i < MAX_ELEMENTS/10) {
        Assert.assertFalse("must be deleted: "+key, data.containsKey(key));
      } else {
        Assert.assertTrue("can find: "+key, data.containsKey(key));
      }
    }

    data.clear();
  }

  @Test
  public void iterationTest() {
    var data = newMap();
    int count = 0;
    for (var it : data) {
      count++;
    }
    Assert.assertEquals("count of elements", MAX_ELEMENTS, count);
    data.clear();
  }

  @Test
  public void containsTest() {
    var data = newMap();
    Assert.assertEquals("contains item", true, data.containsKey("test-1"));
    data.clear();
  }

  @Test
  public void putTest() {
    var data = newMap();
    data.put("test-key", 1);
    Assert.assertEquals("can't add new item", true, data.containsKey("test-key"));
    data.clear();
  }

  @Test
  public void removeNewItemTest() {
    var data = newMap();
    data.put("test-key", 1);
    Assert.assertEquals("can't add new item", true, data.containsKey("test-key"));
    data.remove("test-key");
    Assert.assertEquals("can't remove new item", false, data.containsKey("test-key"));
    data.clear();
  }

  @Test
  public void putmapTest() {
    var data = newMap();
    var map = new HashMap<String, Integer>();
    map.put("x-test-1", 1);
    map.put("x-test-2", 2);
    map.put("x-test-3", 3);
    data.putAll(map);
    Assert.assertEquals("invalid add item test-1", true, data.containsKey("x-test-1"));
    Assert.assertEquals("invalid add item test-2", true, data.containsKey("x-test-2"));
    Assert.assertEquals("invalid add item test-3", true, data.containsKey("x-test-3"));
    data.clear();
  }

  @Test
  public void updateTest() {
    var data = newMap();
    Assert.assertEquals("can't update item", Integer.valueOf(1), data.put("test-1", 101));
    data.clear();
  }

  @Test
  public void removeUnexistsTest() {
    var data = newMap();
    Assert.assertEquals("unexists item is not exists", null, data.remove("undefined"));
    data.clear();
  }

  @Test
  public void keysTest() {
    var data = newMap();
    var keys = data.keySet();
    for (var it : data) {
      Assert.assertEquals("undefined key", true, keys.contains(it.getKey()));
    }
    data.clear();
  }

  @Test
  public void valuesTest() {
    var data = newMap();
    var values = data.values();
    for (var it : data) {
      Assert.assertEquals("undefined key", true, values.contains(it.getValue()));
    }
    data.clear();
  }

  @Test
  public void containsKeyTest() {
    var data = newMap();
    Assert.assertEquals("undefined key", true, data.containsKey("test-100"));
    Assert.assertEquals("invalid key", false, data.containsKey("test-100900"));
    data.clear();
  }

  @Test
  public void containsValueTest() {
    var data = newMap();
    Assert.assertEquals("undefined value", true, data.containsValue(Integer.valueOf(100)));
    Assert.assertEquals("invalid value", false, data.containsValue(Integer.valueOf(100900)));
    data.clear();
  }
}
