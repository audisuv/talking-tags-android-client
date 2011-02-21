// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.android.apps.talkingtags.activities;

import java.util.List;

import com.google.android.apps.talkingtags.model.Tag;

/**
 * Interface for view actions associated with the {@link AdminActivity}.
 * @author adamconnors@google.com (Adam Connors)
 */
public interface AdminView extends StandardView {
  public static final int DIALOG_INITIALISING = 0;
  public static final int DIALOG_NETWORK_ERROR = 1;
  public static final int DIALOG_LOADING = 2;
  public static final int DIALOG_COLLECTION_AVAILABLE_OPTIONS = 3;
  public static final int DIALOG_COLLECTION_UNAVAILABLE_OPTIONS = 4;

  void showTags(List<Tag> tags);
  void showCollections();
}
