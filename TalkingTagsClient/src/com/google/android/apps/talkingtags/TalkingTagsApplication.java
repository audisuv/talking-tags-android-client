/**
 * 
 */
package com.google.android.apps.talkingtags;

import android.app.Application;
import android.os.Handler;

import com.google.android.apps.talkingtags.datasources.TextDataSource;
import com.google.android.apps.talkingtags.datasources.UrlTextDataSource;
import com.google.android.apps.talkingtags.model.CollectionStore;
import com.google.android.apps.talkingtags.model.Tag;
import com.google.android.apps.talkingtags.model.TagCollection;
import com.google.android.apps.talkingtags.model.TagStore;
import com.google.android.apps.talkingtags.network.NetworkFetcher;
import com.google.android.apps.talkingtags.network.Request;
import com.google.android.apps.talkingtags.persistence.InMemoryStore;
import com.google.android.apps.talkingtags.persistence.Store;
import com.google.android.apps.talkingtags.threading.SimpleThreadQueue;
import com.google.android.apps.talkingtags.threading.ThreadEndpoint;
import com.google.android.apps.talkingtags.threading.ThreadQueue;

/**
 * Core Android Application class instantiated by the system 
 * when any activity or service is started up.
 * 
 * Chain of instantiation runs like this:
 *   - System calls onCreate() for TalkingTagsApplication
 *   - This class instantiates and sets up Store / Fetcher threads etc.
 *   - Activity onCreate() is called which retrieves core classes from this app.
 * 
 * @author adamconnors
 */
public class TalkingTagsApplication extends Application {
  
  private Controller ctrl;
  private Handler uiThreadHandler = new Handler();
  
  @Override
  public void onCreate() {
    super.onCreate();
    
    // Create datastores for tags and collections.
    // TODO(adamconnors): Replace InMemoryStore with something persistent.
    Store<Tag> tagStore = new TagStore(new InMemoryStore());
    Store<TagCollection> collectionStore = new CollectionStore(new InMemoryStore());

    // Create the network requester based on a Text file Url reader.
    // TODO(adamconnors): A more scalable way of storing and retrieving tags will need a more
    // elaborate fetcher.
    TextDataSource urlDataSource = new UrlTextDataSource();
    NetworkFetcher fetcher = new NetworkFetcher(urlDataSource, networkResponseEndpoint);
    
    // Create network thread.
    ThreadQueue<Request> network = new SimpleThreadQueue<Request>("tt-network", fetcher);
    network.start();
    
    ctrl = new Controller(collectionStore, tagStore, network);
  }

  /**
   * Returns the controller for this application.
   */
  public Controller getController() {
    return ctrl;
  }
  
  // Receives messages back from the network thread and puts them back on the ui thread.
  private ThreadEndpoint<Request> networkResponseEndpoint = new ThreadEndpoint<Request>() {
    @Override
    public void execute(final Request req) {
      uiThreadHandler.post(new Runnable() {
        @Override
        public void run() {
          ctrl.onNetworkResponse(req);
        }
      });
    }
  };
}
