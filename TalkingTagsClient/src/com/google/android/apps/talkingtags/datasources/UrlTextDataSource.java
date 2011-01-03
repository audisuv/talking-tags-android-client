/**
 * 
 */
package com.google.android.apps.talkingtags.datasources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Implementation of TextDataSource that gets data from a Url.
 * @author adamconnors
 */
public class UrlTextDataSource implements TextDataSource {
  @Override
  public ArrayList<String> getData(String resource) throws IOException {
    URL u = new URL(resource);
    ArrayList<String> lines = new ArrayList<String>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(u.openStream()));
    String line;
    while ( (line = reader.readLine()) != null) {
      if (line.length() == 0) {
        continue;
      }

      lines.add(line);
    }
    return lines;
  }
}
