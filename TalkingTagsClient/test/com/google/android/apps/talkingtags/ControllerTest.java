/**
 * 
 */
package com.google.android.apps.talkingtags;

import junit.framework.TestCase;

import com.google.android.apps.talkingtags.model.TagCollection;
import com.google.android.apps.talkingtags.persistence.Store;

/**
 * @author adamconnors
 */
public class ControllerTest extends TestCase {

  private FakeApplication app;
  private Controller ctrl;
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    Config.setTest(true);
    this.app = new FakeApplication();
    ctrl = app.ctrl;
  }
  
  public void testAdminViewResumeLoadsCollections() throws Exception {
    
    // Set-up View Expectations.
    // Need to get the dependencies right before this works in eclipse :(
//    app.fakeView.showDialog(AdminView.DIALOG_INITIALISING);
//    app.fakeView.onDataUpdated();
//    EasyMock.replay(app.fakeView);
    
    Store<TagCollection> collections = ctrl.getTagCollectionStore();
    assertEquals(0, collections.size());
    ctrl.onAdminActivityResume();
    assertEquals(1, collections.size());
    assertTrue(app.getFakeServer().getLastRequest().contains(Config.COLLECTIONS_RESOURCE));
    assertNotNull(ctrl.getTagCollectionStore().read("test-collection"));
    
//    EasyMock.verify(app.fakeView);
  }
  
  public void testAdminViewSynchronizeCollection() throws Exception {
    ctrl.onAdminActivityResume();
    TagCollection testCollection = ctrl.getTagCollectionStore().read("test-collection");
    assertFalse(testCollection.isAvailable());
    ctrl.synchronize(ctrl.getTagCollectionStore().read(0));
    
    testCollection = ctrl.getTagCollectionStore().read("test-collection");
    assertTrue(testCollection.isAvailable());
    
    assertEquals("test-collection", ctrl.getTagCollectionStore().read(0).name);
    assertEquals("00008117", ctrl.getTagStore().read(0).tagId);
  }
}
