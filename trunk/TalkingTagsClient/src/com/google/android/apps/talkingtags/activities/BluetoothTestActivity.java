package com.google.android.apps.talkingtags.activities;

import com.google.android.apps.talkingtags.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

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
    log("Bonded devices...");
    Set<BluetoothDevice> bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
    BluetoothDevice rfidReader = null;
    for (BluetoothDevice d : bondedDevices) {
      log("Found paired device: " + d.getName());
      if (d.getName().startsWith("RN")) {
        rfidReader = d;
        break;
      }
    }

    if (rfidReader != null) {
      log("Found a paired device with name RN***. Connecting to reader...");
    } else {
      log("No paired device with name RN*** found. Is your reader paired?");
      return;
    }

    BluetoothSocket socket = null;
    try {
      socket = rfidReader.createRfcommSocketToServiceRecord(STANDARD_SERIAL_UUID);
      log("Got socket to reader, connecting...");
      socket.connect();
      log("Connected! Making request...");
      String response = makeRequest(socket);
      log("Got version response: " + response);
    } catch (IOException e) {
      log("IO Exception: " + e.getMessage());
    } finally {
      if (socket != null) {
        try {
          socket.close();
        } catch (Exception e) {
          // ignore.
        }
      }
    }
  }

  private String makeRequest(BluetoothSocket socket) throws IOException {
    InputStream in = socket.getInputStream();
    OutputStream out = socket.getOutputStream();
    byte[] requestFirmwareVersion = { 0x0A, (byte) 0xFF, 0x02, 0x22, (byte) 0xD3 };
    log("Writing request firmware...");
    out.write(requestFirmwareVersion);
    out.flush();
    byte[] response = new byte[7];
    log("Reading response...");
    in.read(response);
    in.close();
    return toHexString(response);
  }

  private String toHexString(byte[] response) {
    StringBuilder rtn = new StringBuilder();
    for (byte b : response) {
      rtn.append("0x").append(Integer.toHexString(b)).append(" ");
    }
    return rtn.toString();
  }

  private void log(String s) {
    ViewGroup g = (ViewGroup) findViewById(R.id.log);
    TextView t = new TextView(this);
    t.setText(s);
    t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
    g.addView(t);
  }
}
