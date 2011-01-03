package com.google.android.apps.talkingtags.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple in-memory persistence. Slow and ugly, useful for testing and debug only.
 * 
 * TODO(adamconnors): Replace with SQL or disk based store.
 * 
 * @author adamconnors
 */
public class InMemoryStore implements Store<byte[]> {

  private class Payload {
    public final String key;
    public final byte[] data;
    public final String group;
    public Payload(String key, byte[] data, String group) {
      this.key = key;
      this.data = data;
      this.group = group;
    }

    @Override
    public boolean equals(Object o) {
      return key.equals(((Payload)o).key);
    }

    @Override
    public int hashCode() {
      return key.hashCode();
    }

    @Override
    public String toString() {
      return key + "=" + new String(data);
    }
  }
  
  private ArrayList<Payload> store = new ArrayList<Payload>();
  
  @Override
  public void clear() {
    store.clear();
  }

  @Override
  public byte[] read(String key) {
    for (Payload p : store) {
      if (key.equals(p.key)) {
        return p.data;
      }
    }
    return null;
  }
  
  @Override
  public byte[] read(int pos) {
    return store.get(pos).data;
  }

  @Override
  public List<byte[]> readAll(String group) {
    ArrayList<byte[]> rtn = new ArrayList<byte[]>();
    for (Payload p : store) {
      if (group.equals(p.group)) {
        rtn.add(p.data);
      }
    }
    return rtn;
  }

  @Override
  public void write(String key, byte[] value) {
    write(key, value, null);
  }

  @Override
  public void write(String key, byte[] value, String group) {
    Payload p = new Payload(key, value, group);
    store.remove(p);
    store.add(p);
  }

  @Override
  public int size() {
    return store.size();
  }
}
