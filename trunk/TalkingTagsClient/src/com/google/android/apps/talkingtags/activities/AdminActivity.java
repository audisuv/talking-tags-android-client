// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.android.apps.talkingtags.activities;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.apps.talkingtags.Controller;
import com.google.android.apps.talkingtags.R;
import com.google.android.apps.talkingtags.TalkingTagsApplication;
import com.google.android.apps.talkingtags.listadapters.CollectionListAdapter;
import com.google.android.apps.talkingtags.listadapters.TagListAdapter;
import com.google.android.apps.talkingtags.model.Tag;
import com.google.android.apps.talkingtags.model.TagCollection;

/**
 * Activity for displaying available tag collections, synchronizing tag-data
 * and browing available tags.
 * 
 * @author adamconnors
 */
public class AdminActivity extends ListActivity implements AdminView {
    
  private Controller ctrl;
  private CollectionListAdapter collectionAdapter;
  private TagListAdapter tagAdapter;
  
  // The collection for which the current dialog is currently being displayed.
  private TagCollection currentCollection;
  
  private static String[] OPTIONS_AVAILABLE;
  private static String[] OPTIONS_UNAVAILABLE;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ctrl = ((TalkingTagsApplication) getApplication()).getController();
    ctrl.setAdminView(this);
  
    OPTIONS_AVAILABLE = new String[] 
        { getString(R.string.copt_show), getString(R.string.copt_remove) };
    
    OPTIONS_UNAVAILABLE = new String[] { getString(R.string.copt_synchronize) };
    
    collectionAdapter = new CollectionListAdapter(this, ctrl.getTagCollectionStore());
    tagAdapter = new TagListAdapter(this);
    setContentView(R.layout.adminview);
    setListAdapter(collectionAdapter);
  }
  
  @Override
  protected void onListItemClick(ListView list, View item, int position, long id) {
    if (getListAdapter().equals(tagAdapter)) {
      // We are showing a tag, read content.
      Intent readTag = new Intent(this, TagActivity.class);
      readTag.putExtra(TagActivity.EXTRA_ID, ctrl.getTagStore().read(position).tagId);
      startActivity(readTag);
    } else {
      // We are showing the collections.
      showCollectionOptions(ctrl.getTagCollectionStore().read(position));
    }
  }
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
      if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        if (getListAdapter().equals(tagAdapter)) {
          showCollections();
          return true;
        }
      }
      
      return super.onKeyDown(keyCode, event);
  }
  @Override
  protected void onResume() {
    super.onResume();
    ctrl.onAdminActivityResume();
  }

  @Override
  public void onDataUpdated() {
    collectionAdapter.notifyDataSetChanged();
    setTitle(getString(R.string.admin_title, ctrl.getTagCollectionStore().size()));
  }

  @Override
  protected Dialog onCreateDialog(int id) {
    switch (id) {
      case AdminView.DIALOG_INITIALISING:
      case AdminView.DIALOG_LOADING:
        return ProgressDialog.show(this, "", 
            (id == DIALOG_INITIALISING) ? getString(R.string.initialising)
             : getString(R.string.loading), true);
        
      case AdminView.DIALOG_NETWORK_ERROR:
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.network_error)
               .setCancelable(false)
               .setNeutralButton(R.string.okay, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        AdminActivity.this.finish();
                   }
               });
        return builder.create();
        
      case AdminView.DIALOG_COLLECTION_UNAVAILABLE_OPTIONS:
      case AdminView.DIALOG_COLLECTION_AVAILABLE_OPTIONS:
        AlertDialog.Builder options = new AlertDialog.Builder(this);
        int title = (currentCollection.isAvailable()) 
            ? R.string.copt_title_available : R.string.copt_title_unavailable;
        options.setTitle(getString(title));
        setItems(options, currentCollection, id);
        return options.create();   

      default:
        return null;
    }
  }
  
  /**
   * Sets the option items and actions based on collection state.
   */
  private void setItems(Builder options, final TagCollection col, final int dialogId) {
    options.setItems( (col.isAvailable()) ? OPTIONS_AVAILABLE : OPTIONS_UNAVAILABLE, 
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int item) {
            dismissDialog(dialogId);
            switch (item) {
              case 0:
                if (col.isAvailable()) {
                  ctrl.show(col);
                } else {
                  ctrl.synchronize(col);
                }
              case 1:
                // only defined when available.
                ctrl.remove(col);
            }
          }
        });
  }

  private void showCollectionOptions(TagCollection col) {
    currentCollection = col;
    showDialog(col.isAvailable() 
        ? DIALOG_COLLECTION_AVAILABLE_OPTIONS 
        : DIALOG_COLLECTION_UNAVAILABLE_OPTIONS);
  }

  @Override
  public void showToast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showToast(int id) {
    showToast(getString(id));
  }

  @Override
  public void showTags(List<Tag> tags) {
    tagAdapter.setTags(tags);
    setListAdapter(tagAdapter);
  }

  @Override
  public void showCollections() {
    setListAdapter(collectionAdapter);
  }
}
