package com.google.android.apps.talkingtags;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.android.apps.talkingtags.datasources.TextDataSource;

/**
 * A fake data source that returns sensible sample data for the supported requests the client
 * might make, and allows assertions to be placed on the requests it ahs received.
 * 
 * @author adamconnors
 */
public class FakeDataSource implements TextDataSource {
  private static final String COLLECTION_DATA 
      = "test-collection: collection-type=test-tags-url";
  
  private static final String TAG_DATA = "00008117=You are now at street level and on the North " +
  		"side of Oxford Street.  You are standing near the top of the steps to Marble Arch. ";
  
  private String lastResourceRequest;
  
  public String getLastRequest() {
    return lastResourceRequest;
  }
  
  @Override
  public List<String> getData(String resourcename) throws IOException {
    this.lastResourceRequest = resourcename;
    if (resourcename.contains(Config.COLLECTIONS_RESOURCE)) {
      // A collections response.
      return Arrays.asList(COLLECTION_DATA);
    } else {
      // Assume everything else is a tag request for now.
      return Arrays.asList(TAG_DATA);
    }
  }
}
