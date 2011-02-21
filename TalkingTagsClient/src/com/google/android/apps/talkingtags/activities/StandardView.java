package com.google.android.apps.talkingtags.activities;

/**
 * Base interface shared by all views in the app.
 *
 * @author adamconnors@google.com (Adam Connors)
 */
public interface StandardView {
  void onDataUpdated();
  void showDialog(int id);
  void dismissDialog(int id);
  void showToast(String msg);
  void showToast(int resourceId);
}
