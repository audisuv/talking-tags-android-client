package com.google.android.apps.talkingtags.activities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.apps.talkingtags.BluetoothRfidListener;
import com.google.android.apps.talkingtags.R;

/**
 * Simple bluetooth diagnostic activity.
 * @author adamconnors
 */
public class BluetoothTestActivity extends Activity {

  // This is the standard UUID used by serial boards.
  private static final UUID STANDARD_SERIAL_UUID
      = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.bluetooth_test);
  }

  @Override
  protected void onResume() {
    super.onResume();
    BluetoothRfidListener rfid = new BluetoothRfidListener();
    log("connecting...");
    rfid.connect();
    log("connected");
  }

  private void log(String s) {
    ViewGroup g = (ViewGroup) findViewById(R.id.log);
    TextView t = new TextView(this);
    t.setText(s);
    t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
    g.addView(t);
  }
}
