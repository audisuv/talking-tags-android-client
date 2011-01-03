/**
 * 
 */
package com.google.android.apps.talkingtags.datasources;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Provides data from a static string. Used for tests.
 * @author adamconnors
 */
public class StaticTextDataSource implements TextDataSource {

  private String data;
  
  public StaticTextDataSource(String data) {
    this.data = data;
  }
  
  @Override
  public ArrayList<String> getData(String resourcename) throws IOException {
    String[] lines = data.split("\n");
    ArrayList<String> rtn = new ArrayList<String>(lines.length);
    for (String line : lines) {
      rtn.add(line);
    }
    return rtn;
  }
}
