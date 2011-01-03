// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.android.apps.talkingtags.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.apps.talkingtags.model.Tag;

/**
 * Simple Fetcher that downloads the simple OxTag from the specified server location.
 * @author adamconnors@google.com (Adam Connors)
 */
public class OxTagDataParser implements TagParser {
  @Override
  public List<Tag> parseTags(List<String> lines) throws IOException {
    ArrayList<Tag> tags = new ArrayList<Tag>(lines.size());
    for (String line : lines) {
      String[] parts = line.split("=");
      if (parts.length != 2) {
        throw new IOException("Unexpected tag line format: " + line);
      }

      // TODO(adamconnors): Fill out additional data for each tag.
      int fullstop = parts[1].indexOf(".");

      // Kind of a hack.
      String title = (fullstop != -1) ? parts[1].substring(0, fullstop + 1) : parts[1];
      String body = (fullstop != -1) ? parts[1].substring(fullstop + 1, parts[1].length()) : "";

      Tag t = new Tag(parts[0], "", title, body);
      tags.add(t);
    }

    return tags;
  }
}
