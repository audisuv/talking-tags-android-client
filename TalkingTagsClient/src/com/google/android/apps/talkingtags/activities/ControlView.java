package com.google.android.apps.talkingtags.activities;

import java.util.List;

public interface ControlView extends StandardView {
  public static final int DIALOG_CONNECTING = 0;

  /**
   * Displays the ids of nearby tags.
   */
  void setNearbyTags(List<String> tags);
}
