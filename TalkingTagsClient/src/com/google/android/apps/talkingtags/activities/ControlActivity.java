/**
 *
 */
package com.google.android.apps.talkingtags.activities;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.google.android.apps.talkingtags.Controller;
import com.google.android.apps.talkingtags.R;
import com.google.android.apps.talkingtags.TalkingTagsApplication;

/**
 * Entry point activity for controlling the RFID service and browsing
 * the tag collections.
 *
 * @author adamconnors
 */
public class ControlActivity extends Activity implements ControlView {

  private ListView list;
  private TextView nearbyTag;
  
  private String options_off[]= { "Browse Tags", "Start Listening" };
  private String options_on[]= { "Browse Tags", "Stop Listening" };
  private Controller ctrl;
  private String nearbyTagData;

  /**
   * Ordinal positions of options in the options arrays.
   */
  private static final int BROWSE = 0;
  private static final int TOGGLE_SERVICE = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ctrl = ((TalkingTagsApplication) getApplication()).getController();
    ctrl.setControlView(this);

    setContentView(R.layout.controlview);
    list = (ListView) findViewById(R.id.list);
    nearbyTag = (TextView) findViewById(R.id.nearbyTag);
    
    list.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
        switch (pos) {
        case BROWSE:
          // Show the admin activity.
          Intent admin = new Intent(ControlActivity.this, AdminActivity.class);
          startActivity(admin);
          return;
        case TOGGLE_SERVICE:
          ctrl.updateRfidServiceState(!ctrl.isRfidServiceRunning());
          return;
        }
      }
    });
    
    nearbyTag.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        ctrl.onShowTag(nearbyTagData);
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    onDataUpdated();
  }

  @Override
  protected Dialog onCreateDialog(int id) {
    switch (id) {
      case ControlView.DIALOG_CONNECTING:
        return ProgressDialog.show(this, "", getString(R.string.connecting), true);
      default:
        return null;
    }
  }


  @Override
  public void onDataUpdated() {
    list.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 ,
        getOptions()));
  }

  private String[] getOptions() {
    return (ctrl.isRfidServiceRunning()) ? options_on : options_off;
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
  public void setNearbyTags(List<String> tags) {
    // TODO: Show the full list instead of just the first.
    if (tags == null || tags.size() == 0) {
      nearbyTag.setText(R.string.no_tags);
    } else {
      nearbyTagData = tags.get(0);
      nearbyTag.setText(nearbyTagData);
    }
  }
}
