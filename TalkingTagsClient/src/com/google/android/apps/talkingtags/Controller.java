// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.android.apps.talkingtags;

import java.util.List;

import com.google.android.apps.talkingtags.activities.AdminView;
import com.google.android.apps.talkingtags.model.Tag;
import com.google.android.apps.talkingtags.model.TagCollection;
import com.google.android.apps.talkingtags.network.CollectionRequest;
import com.google.android.apps.talkingtags.network.Request;
import com.google.android.apps.talkingtags.network.TagRequest;
import com.google.android.apps.talkingtags.persistence.Store;
import com.google.android.apps.talkingtags.threading.ThreadQueue;

/**
 * Controller class for application flow. Instantiated by
 * {@link TalkingTagsApplication} and used by all activities to control
 * application flow.
 *
 * @author adamconnors@google.com (Adam Connors)
 */
public class Controller {
  private AdminView adminView;
  private Store<TagCollection> collectionStore;
  private Store<Tag> tagStore;
  private ThreadQueue<Request> network;
  
  public Controller(Store<TagCollection> collectionStore,
      Store<Tag> tagStore,
      ThreadQueue<Request> network) {
    this.collectionStore = collectionStore;
    this.tagStore = tagStore;
    this.network = network;
  }

  /**
   * AdminActivity has resumed.
   */
  public void onAdminActivityResume() {
    if (collectionStore.size() == 0) {
      adminView.showDialog(AdminView.DIALOG_INITIALISING);
      Request request = new CollectionRequest();
      
      // Make network request on network queue.
      network.post(request);
    }
  }
  
  /**
   * Called whenever the network thread returns.
   */
  public void onNetworkResponse(Request req) {
    
    if (req.getException() != null) {
      adminView.showToast(req.getException().getMessage());
    }
    
    switch (req.type) {
    case Request.TYPE_REQUEST_COLLECTIONS:
      handleCollections((CollectionRequest) req);
      return;
    case Request.TYPE_REQUEST_TAGS:
      handleTags((TagRequest) req);
      return;
    default:
      throw new IllegalStateException("Unrecognized request type: " + req.type);
    }
  }
  
  /**
   * A bunch of tags have been returned by the server.
   *  - Update the local store.
   *  - Update the state of the collection.
   *  - Stop the "loading" spinner.
   *  - Notify the user.
   */
  private void handleTags(TagRequest req) {
    adminView.dismissDialog(AdminView.DIALOG_LOADING);
    List<Tag> tags = req.getTags();
    
    // Write all the tags and associate with the given collection Id.
    for (Tag tag : tags) {
      tagStore.write(tag.tagId, tag, req.getCollectionId());
    }
    
    // Update the state of the tag collection.
    TagCollection item = collectionStore.read(req.getCollectionId());
    if (item != null) {
      item.setAvailable(true);
      collectionStore.write(req.getCollectionId(), item);
    }

    // Show toast so user knows update is complete.
    adminView.showToast(R.string.tagcollection_synchronized);
    adminView.onDataUpdated();
  }

  private void handleCollections(CollectionRequest req) {
    adminView.dismissDialog(AdminView.DIALOG_INITIALISING);
    for (TagCollection col : req.getCollections()) {
      collectionStore.write(col.name, col);
    }
    
    adminView.onDataUpdated();
  }

  /**
   * Called by the AdminActivity when it is created.
   * @param adminView
   */
  public void setAdminView(AdminView adminView) {
    this.adminView = adminView;
  }

  public Store<TagCollection> getTagCollectionStore() {
    return collectionStore;
  }

  public Store<Tag> getTagStore() {
    return tagStore;
  }

  /**
   * Removes the specified collection from local storage.
   */
  public void remove(TagCollection col) {
  }

  /**
   * Shows the given tag collection tags.
   */
  public void show(TagCollection col) {
    adminView.showTags(tagStore.readAll(col.name));
  }

  /**
   * Synchronizes the given tag collection.
   * TODO(adamconnors): Put spinner on list-item instead of blocking other actions.
   */
  public void synchronize(TagCollection col) {
    adminView.showDialog(AdminView.DIALOG_LOADING);
    network.post(new TagRequest(col.resource, col.name));
  }
}
