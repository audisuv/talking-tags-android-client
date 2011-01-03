/**
 * 
 */
package com.google.android.apps.talkingtags.threading;

/**
 * An in-thread implementation of the thread-queue to be used for tests.
 * 
 * @author adamconnors
 */
public class BlockingThreadQueue<T> implements ThreadQueue<T> {
  
  private ThreadEndpoint<T> endpoint;
  
  public BlockingThreadQueue(ThreadEndpoint<T> endpoint) {
    this.endpoint = endpoint;
  }
  
  @Override
  public void post(T message) {
    endpoint.execute(message);
  }

  @Override
  public void start() {
    // no-op since this queue is synchronous it doesn't really need starting.
  }
}
