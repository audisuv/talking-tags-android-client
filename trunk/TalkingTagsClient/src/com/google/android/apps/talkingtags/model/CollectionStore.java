/**
 * 
 */
package com.google.android.apps.talkingtags.model;

import com.google.android.apps.talkingtags.persistence.Store;

/**
 * Store for collections of tags.
 * @author adamconnors
 */
public class CollectionStore extends BaseStore<TagCollection> {
  public CollectionStore(Store<byte[]> store) {
    super(store);
  }
}
