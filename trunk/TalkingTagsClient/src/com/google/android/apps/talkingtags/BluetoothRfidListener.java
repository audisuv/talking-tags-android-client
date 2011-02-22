package com.google.android.apps.talkingtags;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Handles all the communications with the Bluetooth Rfid listener.
 *
 * @author adamconnors
 */
public class BluetoothRfidListener implements RfidListener {

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

  public BluetoothRfidListener() {
    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
  }

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
      Config.log("Connecting...");
      socket = rfidReader.createRfcommSocketToServiceRecord(STANDARD_SERIAL_UUID);
      socket.connect();
      Config.log("Connected...");
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
//    try {
//      //return getTags();
//    } catch (IOException e) {
//      // TODO: Report this more nicely.
//      ArrayList<String> err = new ArrayList<String>();
//      err.add("Exception reading tags: " + e.getMessage());
//      return err;
//    }
    return null;
  }

  private void getVersion() throws IOException {
    int[] version = makeRequest(new int[] { 0x0A, 0xFF, 0x02, 0x22, 0xD3 });
    Config.log("Got version string: " + toHexString(version));
  }

  // Sends the start polling message to the device.
  private void startPolling() throws IOException {
    int[] start = makeRequest(new int[] { 0x0A, 0x04, 0x02, 0x90, 0x60 });
    if (start.length != 2 || start[0] != 0x00) {
      throw new IOException("Unexpected response from start-polling: " + toHexString(start));
    }
  }

  // Sends the stop polling message to the device.
  private void stopPolling() throws IOException {
    int[] stop = makeRequest(new int[] { 0x0A, 0x04, 0x02, 0x91, 0x5F });
    if (stop.length != 2 || stop[0] != 0x00) {
      throw new IOException("Unexpected response from stop-polling: " + toHexString(stop));
    }
  }

  // Asks the device for its visible tags.
  private List<String> getTags() throws IOException {
    int[] tagResponse = makeRequest(new int[] { 0x0A, 0x04, 0x02, 0x9A, 0x56 });
    if (tagResponse.length < 3 || tagResponse[0] != 0x00) {
      throw new IOException("Error response from getTags: " + toHexString(tagResponse));
    }

    int tagCount = tagResponse[1];
    ArrayList<String> tags = new ArrayList<String>(tagCount);

    // Parse tag data.
    int offset = 2;
    while (offset < tagResponse.length) {
      tags.add(toHexString(tagResponse, offset, 10));
      offset += 10;
    }

    // TODO: Check checksum.
    return tags;
  }

  private int[] makeRequest(int[] command) throws IOException {
    InputStream in = socket.getInputStream();
    OutputStream out = socket.getOutputStream();
    Config.log("Sending: " + toHexString(command));

    // Write as single ints to prevent overflowing the signed bytes.
    for (int i : command) {
      out.write(i);
    }

    out.flush();
    int[] header = new int[3];
    readFully(in, header);

    int length = header[2];
    int[] data = new int[length];
    readFully(in, data);
    return data;
  }

  /**
   * Ghetto -- no buffering, but works around unsigned bytes problem.
   * Blocks until the full expected number of bytes is read.
   */
  private void readFully(InputStream in, int[] buf) throws IOException {
    int count = 0;
    for (int i = 0; i < buf.length; i++) {
      buf[i] = in.read();
    }
  }

  /**
   * Blocks until the full expected number of bytes is read.
   */
  private void readFully(InputStream in, byte[] buf) throws IOException {
    Config.log("Attempting to read: " + buf.length + " bytes.");
    int offset = 0;
    int numRead = 0;
    while (offset < buf.length && (numRead=in.read(buf, offset, buf.length-offset)) >= 0) {
        Config.log("Read " + numRead + " " + toHexString(buf, 0, numRead));
        offset += numRead;
    }

    // Ensure all the bytes have been read in
    if (offset < buf.length) {
        throw new IOException("Failed to read all bytes, wanted "
            + buf.length +" but got " + offset );
    }
  }

  private String toHexString(int[] data, int offset, int length) {
    StringBuilder rtn = new StringBuilder();
    for (int i = offset; i < offset+length; i++) {
      rtn.append(Integer.toHexString(data[i])).append(" ");
    }
    return rtn.toString();
  }

  private String toHexString(byte[] data, int offset, int length) {
    StringBuilder rtn = new StringBuilder();
    for (int i = offset; i < offset+length; i++) {
      rtn.append(Integer.toHexString(data[i])).append(" ");
    }
    return rtn.toString();
  }

  private String toHexString(int[] response) {
    return toHexString(response, 0, response.length);
  }

  private String toHexString(byte[] response) {
    return toHexString(response, 0, response.length);
  }
}
