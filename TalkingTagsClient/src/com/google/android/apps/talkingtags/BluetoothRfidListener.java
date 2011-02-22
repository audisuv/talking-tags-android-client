package com.google.android.apps.talkingtags;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

/**
 * Handles all the communications with the Bluetooth Rfid listener.
 *
 * @author adamconnors
 */
public class BluetoothRfidListener implements RfidListener {

  private Context ctx;
  private State state = State.READY;
  private BluetoothSocket socket;

  /* (non-Javadoc)
   * @see com.google.android.apps.talkingtags.RfidListener#getState()
   */
  public State getState() {
    return state;
  }

  // This is the standard UUID used by serial boards.
  private static final UUID STANDARD_SERIAL_UUID
      = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  private BluetoothAdapter bluetoothAdapter;

  public BluetoothRfidListener(Context ctx) {
    this.ctx = ctx;
    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
  }

  /* (non-Javadoc)
   * @see com.google.android.apps.talkingtags.RfidListener#connect()
   */
  public State connect() {
    Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
    BluetoothDevice rfidReader = null;
    for (BluetoothDevice d : bondedDevices) {
      if (d.getName().startsWith("RN")) {
        rfidReader = d;
        break;
      }
    }

    if (rfidReader == null) {
      Config.log("No bluetooth devices found.");
      state = State.NO_DEVICE_FOUND;
      return state;
    }

    try {
      Config.log("Getting socket...");
      socket = rfidReader.createRfcommSocketToServiceRecord(STANDARD_SERIAL_UUID);
      Config.log("Connecting...");
      socket.connect();
      Config.log("Connected...");
      startPolling();
    } catch (IOException e) {
      Config.log("Couldn't connect to bluetooth: " + e.getMessage());
      state = State.EXCEPTION;
      return state;
    }

    state = State.CONNECTED;
    return state;
  }

  public void reset() {
    Config.log("Bluetooth: Resetting bluetooth connection.");
    if (socket != null) {
      try {
        stopPolling();
        socket.close();
      } catch (IOException e) {
        // Ignore, we're just shutting down.
      } finally {
        socket = null;
      }
    }
    state = State.READY;
  }

  public List<String> ping() {
    return null;
  }
  
  // Sends the start polling message to the device.
  private void startPolling() {
  }
  
  // Sends the stop polling message to the device.
  private void stopPolling() {
  }

  // Asksthe device for its visible tags.
  private void getTags() {
  }
}
