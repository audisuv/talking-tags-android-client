/**
 * 
 */
package com.google.android.apps.talkingtags.persistence;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

/**
 * Test cases for TT's simple persistence interface. This class is subclassed
 * so the InMemory and Disk implementations can be tested based on the same interface.
 * @author adamconnors
 */
public abstract class BaseStoreTest extends TestCase {
  
  public void testSimpleWriteRead() throws Exception {
    Store<byte[]> s = getStore();
    s.write("key1", "value1".getBytes());
    s.write("key2", "value2".getBytes());
    assertEquals("value1", new String(s.read("key1")));
    assertEquals("value2", new String(s.read("key2")));
    assertNull(s.read("key3"));
    
    assertEquals("value2", new String(s.read(1)));
    assertEquals("value1", new String(s.read(0)));
  }
  
  public void testWriteUpdates() throws Exception {
    Store<byte[]> s = getStore();
    s.write("key1", "value1".getBytes());
    s.write("key2", "value2".getBytes());
    s.write("key1", "value1-updated".getBytes());
    
    assertEquals(2, s.size());
    assertEquals("value1-updated", new String(s.read("key1")));
    assertEquals("value2", new String(s.read("key2")));
    assertEquals("value1-updated", new String(s.read(1)));
    assertEquals("value2", new String(s.read(0)));
  }
  
  public void testWriteReadCollections() throws Exception {
    Store<byte[]> s = getStore();
    s.write("key1", "value1".getBytes(), "collection1");
    s.write("key2", "value2".getBytes(), "collection2");
    s.write("key3", "value3".getBytes(), "collection1");
    s.write("key4", "value4".getBytes(), "collection2");

    assertList(Arrays.asList("value1", "value3"),
        s.readAll("collection1"));

    assertList(Arrays.asList("value2", "value4"),
        s.readAll("collection2"));

  }
  
  private void assertList(List<String> lhs, List<byte[]> rhs) {
    assertEquals(lhs.size(), rhs.size());
    for (byte[] b : rhs) {
      assertTrue("lhs doesn't contain: " + new String(b) + ": " + lhs, 
          lhs.contains(new String(b)));
    }
  }

  public abstract Store<byte[]> getStore();
}
