package com.google.android.apps.talkingtags.activities;

public interface ControlView {
  public static final int DIALOG_LOADING = 0;

  void onDataUpdated();
  void showDialog(int id);
  void dismissDialog(int id);
}
