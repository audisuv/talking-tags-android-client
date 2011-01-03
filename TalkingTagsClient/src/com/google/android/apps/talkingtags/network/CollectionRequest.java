/**
 * 
 */
package com.google.android.apps.talkingtags.network;

import java.util.ArrayList;
import java.util.List;

import com.google.android.apps.talkingtags.Config;
import com.google.android.apps.talkingtags.model.TagCollection;
import com.google.android.apps.talkingtags.parsers.CollectionParser;

/**
 * @author adamconnors
 */
public class CollectionRequest extends Request {

  private CollectionParser parser = new CollectionParser();
  
  private ArrayList<TagCollection> collections;
  
  private static final String COLLECTION_LIST_URL 
      = Config.BASE_SERVER_PATH + Config.COLLECTIONS_RESOURCE + Config.SERVER_SUFFIX;
  
  public CollectionRequest() {
    super(Request.TYPE_REQUEST_COLLECTIONS, COLLECTION_LIST_URL);
  }

  @Override
  public void parseResponse(List<String> data) {
    collections = parser.getCollections(data);
  }

  public ArrayList<TagCollection> getCollections() {
    return collections;
  }
}
