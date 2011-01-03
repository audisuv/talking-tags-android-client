/**
 * 
 */
package com.google.android.apps.talkingtags.threading;

import java.util.ArrayList;

/**
 * Simple hand-rolled thread queue. This hasn't really been optimized or
 * tested thoroughly -- it should really be replaced by the LooperThreadQueue
 * or some such.
 * 
 * @author adamconnors
 */
public class SimpleThreadQueue<T> extends Thread implements ThreadQueue<T> {

  private ThreadEndpoint<T> endpoint;
  private ArrayList<T> queue = new ArrayList<T>();
  
  public SimpleThreadQueue(String name, ThreadEndpoint<T> endpoint) {
    super(name);
    this.endpoint = endpoint;
  }
  
  @Override
  public void run() {
    while (true) {
      if (isEmpty()) {
        idle();
      }

      T msg;
      synchronized (this) {
        msg = queue.remove(0);
      }
      endpoint.execute(msg);
    }
  }

  private synchronized boolean isEmpty() {
    return queue.isEmpty();
  }

  private synchronized void idle() {
    try {
      wait();
    } catch (InterruptedException e) {
      // expected.
    }
  }

  @Override
  public void post(T message) {
    synchronized (this) {
      queue.add(message);
      notifyAll();
    }
  }

  // Visible for test.
  public void waitUntilEmpty() {
    while (!queue.isEmpty()) {
      try { Thread.sleep(100); } catch (InterruptedException e) {}
    }
  }
}
