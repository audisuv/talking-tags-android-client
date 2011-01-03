/**
 * 
 */
package com.google.android.apps.talkingtags.network;

import java.io.IOException;
import java.util.List;

/**
 * Concrete message type for requests to the network.
 * 
 * @author adamconnors
 */
public abstract class Request {
  
  public static final int TYPE_REQUEST_COLLECTIONS = 0;
  public static final int TYPE_REQUEST_TAGS = 1;
  
  /**
   * The actual Url being requested.
   */
  public final String url;

  /**
   * Request type.
   */
  public final int type;
  
  /**
   * Exception set if the request fails.
   */
  private Exception exception;
  
  public Request(int type, String url) {
    this.type = type;
    this.url = url;
  }

  /**
   * Called when the response comes back from the network. Subclasses
   * should implement this to handle the incoming data.
   */
  public abstract void parseResponse(List<String> data);
  
  /**
   * Returns any exception associated with this request or null if the request
   * was successful.
   */
  public Exception getException() {
    return exception;
  }

  public void setException(IOException e) {
    this.exception = e;
  }
}
