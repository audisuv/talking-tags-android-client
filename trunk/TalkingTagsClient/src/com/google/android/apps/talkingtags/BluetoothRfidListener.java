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
public class BluetoothRfidListener {

  public enum BluetoothState {
    READY, // Nothing is connected, nothing has happened.
    CONNECTING, // We are starting to connect.
    NO_DEVICE_FOUND, // No devices matching the expected SID was found.
    CONNECTED, // We successfully connected and have a socket.
    EXCEPTION // There was an exception talking to the device.
  }

  private Context ctx;
  private BluetoothState state = BluetoothState.READY;
  private BluetoothSocket socket;

  public BluetoothState getState() {
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

  /**
   * Connects to the bluetooth listener and sends a test message.
   */
  public BluetoothState connect() {
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
      state = BluetoothState.NO_DEVICE_FOUND;
      return state;
    }

    try {
      Config.log("Getting socket...");
      socket = rfidReader.createRfcommSocketToServiceRecord(STANDARD_SERIAL_UUID);
      Config.log("Connecting...");
      socket.connect();
      Config.log("Connected...");
    } catch (IOException e) {
      Config.log("Couldn't connect to bluetooth: " + e.getMessage());
      state = BluetoothState.EXCEPTION;
      return state;
    }

    state = BluetoothState.CONNECTED;
    return state;
  }

  /**
   * Request from the listener service to tidy up so we can try again later.
   * Clear up any instance resources, etc.
   */
  public void reset() {
    Config.log("Bluetooth: Resetting bluetooth connection.");
    if (socket != null) {
      try {
        socket.close();
      } catch (IOException e) {
        // Ignore, we're just shutting down.
      } finally {
        socket = null;
      }
    }
    state = BluetoothState.READY;
  }

  /**
   * Pings the RFID device to see what tags are visible.
   * @return a list of tagIds that are currently visible.
   */
  public List<String> ping() {
    return null;
  }
}
