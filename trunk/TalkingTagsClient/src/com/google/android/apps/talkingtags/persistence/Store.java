package com.google.android.apps.talkingtags.persistence;

import java.util.List;

/**
 * Simple persistent store interface for storing name=value pairs that can also be indexed by
 * additional "groups" -- suitable for our tags+collections needs.
 * 
 * @author adamconnors
 */
public interface Store<T> {
  void write(String key, T value);
  T read(String key);
  T read(int pos);
  void clear();
  void write(String key, T bytes, String group);
  List<T> readAll(String group);
  int size();
}
