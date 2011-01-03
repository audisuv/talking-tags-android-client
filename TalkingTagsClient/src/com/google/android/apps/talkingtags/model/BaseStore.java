package com.google.android.apps.talkingtags.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.android.apps.talkingtags.persistence.Store;

/**
 * Wraps a {@link Store} object (which does the actual persistence part)
 * and handles the serialising / deserialising part. This baseclass is then
 * subclassed to apply the appropriate type.
 *
 * @author adamconnors
 *
 */
public class BaseStore<T> implements Store<T> {

  private final Store<byte[]> store;
  
  public BaseStore(Store<byte[]> store) {
    this.store = store;
  }
  
  @Override
  public void clear() {
    store.clear();
  }

  @Override
  public T read(String key) {
    return convert(store.read(key));
  }

  @Override
  public T read(int pos) {
    return convert(store.read(pos));
  }

  @Override
  public List<T> readAll(String group) {
    List<byte[]> data = store.readAll(group);
    if (data == null) {
      return null;
    }
    
    ArrayList<T> rtn = new ArrayList<T>(data.size());
    for (byte[] d : data) {
      rtn.add(convert(d));
    }
    
    return rtn;
  }

  @Override
  public void write(String key, T value) {
    store.write(key, convert(value));
  }

  @Override
  public void write(String key, T value, String group) {
    store.write(key, convert(value), group);
  }

  @Override
  public int size() {
    return store.size();
  }
  
  @SuppressWarnings("unchecked")
  public T convert(byte[] data) {
    
    if (data == null) {
      return null;
    }
    
    try {
      ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
      return (T) in.readObject();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public byte[] convert(T obj) {
    
    if (obj == null) {
      return null;
    }
    
    try {
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bout);
      out.writeObject(obj);
      return bout.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
