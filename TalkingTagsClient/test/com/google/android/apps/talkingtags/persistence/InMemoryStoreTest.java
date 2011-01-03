/**
 * 
 */
package com.google.android.apps.talkingtags.persistence;

/**
 * @author adamconnors
 */
public class InMemoryStoreTest extends BaseStoreTest {
  @Override
  public Store<byte[]> getStore() {
    return new InMemoryStore();
  }
}
