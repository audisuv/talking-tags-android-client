/**
 * 
 */
package com.google.android.apps.talkingtags.threading;

import com.google.android.apps.talkingtags.Config;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Uses the default Android looper to process a message queue. All this
 * shinanaghans is mostly to save the logic of the application depending
 * on core Android classes so it can be tested more easily.
 * 
 * Nb: Couldn't get this working for some reason. 
 * 
 * @deprecated Can't get this working. Not currently used.
 * @author adamconnors
 */
public class LooperThreadQueue<T> extends Thread implements ThreadQueue<T> {
  private Handler handler;
  private ThreadEndpoint<T> endpoint;
  
  /**
   * Constructs the message queue with a given endpoint. This (typed) message
   * queue is now Android and thread agnostic can be passed to the controller
   * without making everything dastardly hard to test.
   * 
   * @param name String The name of this thread.
   * @param endpoint ThreadEndpoint The actual class that will execute this message.
   */
  public LooperThreadQueue(String name, ThreadEndpoint<T> endpoint) {
    super(name);
    this.endpoint = endpoint;
  }
  
  public void run() {
      Config.log("looper: Started thread - " + Thread.currentThread().getName());
      Looper.prepare();
      
      handler = new Handler() {
          @SuppressWarnings("unchecked")
          public void handleMessage(Message msg) {
            Config.log("looper: Handling message: " + msg + " : " 
                + Thread.currentThread().getName());
            T threadMessage = (T) msg.obj;
            endpoint.execute(threadMessage);
          }
      };
      
      android.util.Log.d("tt", "Created handler: " + handler 
          + " for thread: " + handler.getLooper().getThread().getName());
      
      Looper.loop();
  }
  
  /**
   * Dispatches a message onto the message queue.
   */
  @Override
  public void post(T message) {
    Message msg = handler.obtainMessage();
    android.util.Log.d("tt", "msg for handler: " + msg.getTarget());
    msg.obj = message;
    Config.log("looper: Dispatch message: " + message + " - " 
        + Thread.currentThread().getName());
    handler.dispatchMessage(msg);
    Config.log("looper: Dispatched message: " + message);
  }
}
