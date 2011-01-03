package com.google.android.apps.talkingtags.parsers;

import java.util.ArrayList;
import java.util.List;

import com.google.android.apps.talkingtags.model.TagCollection;
import com.google.android.apps.talkingtags.model.TagCollection.Type;

/**
 * Parses the text file that contains the list of available collections.
 * 
 * Simple format to begin with (should really be XML or some such):
 * 
 * name: type=[url]
 * 
 * @author adamconnors
 */
public class CollectionParser {
  public ArrayList<TagCollection> getCollections(List<String> lines) {
    ArrayList<TagCollection> rtn = new ArrayList<TagCollection>(lines.size());
    for (String line : lines) {
      TagCollection collection = parseLine(line);
      if (collection != null) {
        rtn.add(collection);
      }
    }
    return rtn;
  }

  private TagCollection parseLine(String line) {
    int namePart = line.indexOf(": ");
    int typePart = line.indexOf("=");
    String name = line.substring(0, namePart);
    String type = line.substring(namePart + 1, typePart);
    String resource = line.substring(typePart + 1);
    
    return new TagCollection(name, resource, getType(type));
  }

  private Type getType(String type) {
    try {
      return Type.valueOf(type.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
