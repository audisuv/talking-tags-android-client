package com.google.android.apps.talkingtags.model;

import java.io.Serializable;

/**
 * A Collection represents a named collection of tags that should all be synchronised at the
 * same time.
 * 
 * @author adamconnors
 */
public class TagCollection implements Serializable {
  
  private static final long serialVersionUID = 1L;

  public enum Type {
    OXTAG;
  }
  
  public final String name;
  public final String resource;
  public final Type type;
  private boolean available;
  
  public TagCollection(String name, String resource, Type type) {
    this.name = name;
    this.resource = resource;
    this.type = type;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }
  
  public boolean isAvailable() {
    return available;
  }

  @Override
  public String toString() {
    return name + " (" + resource + ") - available: " + isAvailable();
  }
}
