/**
 *
 */
package com.google.android.apps.talkingtags;


import com.google.android.apps.talkingtags.activities.AdminView;
import com.google.android.apps.talkingtags.datasources.TextDataSource;
import com.google.android.apps.talkingtags.datasources.UrlTextDataSource;
import com.google.android.apps.talkingtags.model.CollectionStore;
import com.google.android.apps.talkingtags.model.TagStore;
import com.google.android.apps.talkingtags.model.Tag;
import com.google.android.apps.talkingtags.model.TagCollection;
import com.google.android.apps.talkingtags.network.NetworkFetcher;
import com.google.android.apps.talkingtags.network.Request;
import com.google.android.apps.talkingtags.parsers.CollectionParser;
import com.google.android.apps.talkingtags.parsers.OxTagDataParser;
import com.google.android.apps.talkingtags.parsers.TagParser;
import com.google.android.apps.talkingtags.persistence.InMemoryStore;
import com.google.android.apps.talkingtags.persistence.Store;
import com.google.android.apps.talkingtags.threading.BlockingThreadQueue;
import com.google.android.apps.talkingtags.threading.ThreadEndpoint;
import com.google.android.apps.talkingtags.threading.ThreadQueue;

import org.easymock.EasyMock;

/**
 * Creates all the necessary parts for testing.
 * @author adamconnors
 */
public class FakeApplication {

  public final ThreadQueue<Request> network;
  public final Store<Tag> tagStore;
  public final Store<TagCollection> collectionStore;
  public final TagParser tagParser;
  public final CollectionParser collectionParser;
  public final TextDataSource tagSource;
  public final Controller ctrl;
  private FakeDataSource server;

  public final AdminView fakeView;
  public final PlatformServices fakeServices;

  public FakeApplication() {
    tagSource = new UrlTextDataSource();
    tagParser = new OxTagDataParser();
    collectionParser = new CollectionParser();
    server = new FakeDataSource();
    network = new BlockingThreadQueue<Request>(new NetworkFetcher(server, networkEndpoint));

    fakeView = EasyMock.createMock(AdminView.class);
    fakeServices = EasyMock.createMock(PlatformServices.class);

    // TODO: This probably needs to come off the UI thread as well.
    tagStore = new TagStore(new InMemoryStore());
    collectionStore = new CollectionStore(new InMemoryStore());

    ctrl = new Controller(collectionStore, tagStore, network, fakeServices);

    ctrl.setAdminView(fakeView);
  }

  public FakeDataSource getFakeServer() {
    return server;
  }

  public final ThreadEndpoint<Request> networkEndpoint = new ThreadEndpoint<Request>() {
    @Override
    public void execute(Request msg) {
      ctrl.onNetworkResponse(msg);
    }
  };

}
