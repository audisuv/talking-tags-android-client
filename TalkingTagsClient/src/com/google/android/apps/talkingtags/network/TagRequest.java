/**
 * 
 */
package com.google.android.apps.talkingtags.network;

import java.io.IOException;
import java.util.List;

import com.google.android.apps.talkingtags.model.Tag;
import com.google.android.apps.talkingtags.parsers.OxTagDataParser;

/**
 * Request for getting the tags associated with a particular Id.
 * @author adamconnors
 */
public class TagRequest extends Request {
  
  private final String collectionId;
  
  private List<Tag> tags;
  
  private final OxTagDataParser parser = new OxTagDataParser();
  
  public TagRequest(String url, String collectionId) {
    super(Request.TYPE_REQUEST_TAGS, url);
    this.collectionId = collectionId;
  }

  public List<Tag> getTags() {
    return tags;
  }

  /**
   * Returns the collection Id for which the tags are requested.
   * @return
   */
  public String getCollectionId() {
    return collectionId;
  }

  @Override
  public void parseResponse(List<String> data) {
    try {
      tags = parser.parseTags(data);
    } catch (IOException e) {
      setException(e);
    }
  }
}
