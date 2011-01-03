// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.android.apps.talkingtags.model;

import java.io.Serializable;

/**
 * Represents a tag-instance.
 * @author adamconnors@google.com (Adam Connors)
 */
public class Tag implements Serializable {
  
  private static final long serialVersionUID = 1L;

  public final String tagId;
  public final String tagShortDescription;
  public final String title;
  public final String body;

  public Tag(String tagId, String shortDesc, String title, String body) {
    this.tagId = tagId;
    this.tagShortDescription = shortDesc;
    this.title = title;
    this.body = body;
  }

  @Override
  public String toString() {
    return "Tag: " + tagId + ": " + title + "/" + body;
  }
}
