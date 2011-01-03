package com.google.android.apps.talkingtags.model;

import com.google.android.apps.talkingtags.persistence.Store;

/**
 * Store for Tag information.
 * @author adamconnors
 */
public class TagStore extends BaseStore<Tag> {
  public TagStore(Store<byte[]> store) {
    super(store);
  }
}
