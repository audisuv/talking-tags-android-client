package com.google.android.apps.talkingtags;

import java.util.List;

import com.google.android.apps.talkingtags.BluetoothRfidListener.BluetoothState;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Service that connects to the RFID bluetooth connection and listens for nearby tags.
 * 
 * @author adamconnors
 */
public class RfidListeningService extends Service implements Runnable {
  
  /**
   * Intent action for starting the listening service.
   */
  public static final String ACTION_START = "start";
  
  /**
   * Intent action for stopping the listening service.
   */
  public static final String ACTION_STOP = "stop";

  /**
   * How often we want to ping the RFID device.
   */
  private static final long PING_INTERVAL = 5000;
  
  private boolean isRunning = false;
  
  private Thread listenThread;
  
  private BluetoothRfidListener rfid;

  private Controller ctrl;
  
  @Override
  public void onCreate() {
    super.onCreate();
    rfid = new BluetoothRfidListener(this);
    ctrl = ((TalkingTagsApplication) getApplication()).getController();
  }

  @Override
  public void onStart(Intent intent, int startId) {
    super.onStart(intent, startId);
    
    if (intent == null || intent.getAction() == null) {
      Config.log("RfidListenerService: Started with no intent. Ignoring.");
      return;
    }
    
    if (intent.getAction().equals(ACTION_START)) {
      startListenLoop();
    } else if (intent.getAction().equals(ACTION_STOP)) {
      stopListenLoop();
    }
  }

  private void stopListenLoop() {
    isRunning = false;
  }

  private void startListenLoop() {
    if (isRunning) {
      return;
    }
    
    isRunning = true;
    listenThread = new Thread(this);
    listenThread.start();
  }

  @Override
  public IBinder onBind(Intent arg0) {
    return null;
  }

  @Override
  public void run() {
    assert rfid.getState() == BluetoothState.READY;
    BluetoothState state = rfid.connect();
    
    if (state != BluetoothState.CONNECTED) {
      Config.log("Couldn't connect, dropping out of ping thread.");
      ctrl.onRfidFailed(state);
      rfid.reset();
      return;
    }
    
    // Bluetooth is now connected. Start pinging it for nearby tags.
    while (isRunning) {
      Config.log("ListenerService: Pinging RFID device for new tags.");
      List<String> tags = rfid.ping();
      ctrl.onRfidGotTags(tags);
      
      Config.log("ListenerService: Sleeping for " + PING_INTERVAL + "ms before next RFID ping.");
      try {
        Thread.sleep(PING_INTERVAL);
      } catch (InterruptedException e) {
        // Ignore.
      }
    }
    
    Config.log("Rfid listener thread existed, resetting rfid connection and exiting.");
    rfid.reset();
  }
}
