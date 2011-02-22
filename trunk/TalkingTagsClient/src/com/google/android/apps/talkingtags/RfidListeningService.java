package com.google.android.apps.talkingtags;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.apps.talkingtags.RfidListener.State;

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

  private RfidListener rfid;
  private Controller ctrl;
  private TalkingTagsApplication app;

  @Override
  public void onCreate() {
    super.onCreate();
    rfid = (Config.isDemoMode()) ? new FakeRfidListener() : new BluetoothRfidListener();
    app = ((TalkingTagsApplication) getApplication());
    ctrl = app.getController();
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
    listenThread.interrupt();
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
    assert rfid.getState() == State.READY;
    Config.log("Connecting to bluetooth...");
    State state = rfid.connect();
    notifyControllerRfidStateChange(state);

    if (state != State.CONNECTED) {
      Config.log("Couldn't connect, dropping out of ping thread.");
      rfid.reset();
      return;
    }

    // Successfully connected.
    Config.log("ListenerService: Connected, entering listen thread.");
    notifyControllerRfidStateChange(state);

    // Bluetooth is now connected. Start pinging it for nearby tags.
    while (isRunning) {
      Config.log("ListenerService: Pinging RFID device for new tags.");
      List<String> tags = rfid.ping();
      notifyControllerGotTags(tags);

      try {
        Thread.sleep(PING_INTERVAL);
      } catch (InterruptedException e) {
        // Ignore.
      }
    }

    Config.log("Rfid listener thread existed, resetting rfid connection and exiting.");
    rfid.reset();
    notifyControllerRfidStateChange(rfid.getState());
  }

  private void notifyControllerGotTags(final List<String> tags) {
    app.postToUi(new Runnable() {
      @Override
      public void run() {
        ctrl.onRfidGotTags(tags);
      }
    });
  }

  private void notifyControllerRfidStateChange(final State state) {
    app.postToUi(new Runnable() {
      @Override
      public void run() {
        ctrl.onRfidStateChange(state);
      }
    });
  }
}
