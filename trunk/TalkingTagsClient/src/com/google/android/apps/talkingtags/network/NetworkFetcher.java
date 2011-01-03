package com.google.android.apps.talkingtags.network;

import java.io.IOException;
import java.util.List;

import com.google.android.apps.talkingtags.Config;
import com.google.android.apps.talkingtags.datasources.TextDataSource;
import com.google.android.apps.talkingtags.threading.ThreadEndpoint;

/**
 * Connects the synchronous DataSource (which makes the actual network connection to the thread
 * endpoints, which take us on/off the network thread.
 * 
 * @author adamconnors
 */
public class NetworkFetcher implements ThreadEndpoint<Request> {

  private final TextDataSource ds;
  
  // Endpoint on the Ui thread for sending data back to the main application.
  private final ThreadEndpoint<Request> endpoint;
  
  /**
   * Constructor.
   * @param ds Datasource that actually makes the request.
   * @param endpoint UI-thread endpoint to receive the response.
   */
  public NetworkFetcher(TextDataSource ds, ThreadEndpoint<Request> endpoint) {
    this.ds = ds;
    this.endpoint = endpoint;
  }
  
  @Override
  public void execute(Request req) {
    try {
      Config.log("network: Fetching data for: " + req.url);
      List<String> data = ds.getData(req.url);
      req.parseResponse(data);
    } catch (IOException e) {
      req.setException(e);
    }
    
    // Send the result back to the Ui thread.
    endpoint.execute(req);
  }
}
