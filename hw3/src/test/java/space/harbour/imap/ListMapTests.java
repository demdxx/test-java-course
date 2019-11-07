package space.harbour.imap;

import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import space.harbour.imap.ListMap;

public class ListMapTests {
  static final int MAX_ELEMENTS = 1000;

  Map<String, Integer> data;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    data = new ListMap<>();
    for (int i = 0; i < MAX_ELEMENTS; i++) {
      data.put(String.format("test-%d", i), i);
    }
  }

  @After
  public void tearDown() throws Exception {
    data.clear();
  }

  @Test
  public void containsTests() {
    Assert.assertEquals("Size incorrect", MAX_ELEMENTS, data.size());

    for (int i = 0; i < MAX_ELEMENTS; i++) {
      var key = String.format("test-%d", i);
      Assert.assertTrue("can find: "+key, data.containsKey(key));
    }
  }

  @Test
  public void removeTest() {
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
  }
}
