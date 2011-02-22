package com.google.android.apps.talkingtags;

import java.util.List;

public interface RfidListener {

  public enum State {
    READY, // Nothing is connected, nothing has happened.
    CONNECTING, // We are starting to connect.
    NO_DEVICE_FOUND, // No devices matching the expected SID was found.
    CONNECTED, // We successfully connected and have a socket.
    EXCEPTION // There was an exception talking to the device.
  }

  State getState();

  /**
   * Connects to the bluetooth listener and sends a test message.
   */
  State connect();

  /**
   * Request from the listener service to tidy up so we can try again later.
   * Clear up any instance resources, etc.
   */
  void reset();

  /**
   * Pings the RFID device to see what tags are visible.
   * @return a list of tagIds that are currently visible.
   */
  List<String> ping();
}