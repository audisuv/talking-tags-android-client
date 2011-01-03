/**
 * 
 */
package com.google.android.apps.talkingtags.threading;

/**
 * Common interface for actions that need to happen off the main UI
 * thread. The {@link LooperThreadQueue} is instantiated by the application
 * which uses the standard Android message queue to shift activities like
 * disk and network I/O onto secondardy threads. 
 * 
 * In tests, the {@link BlockingThreadQueue} can be used to test everything on the 
 * same thread.
 * 
 * @author adamconnors
 */
public interface ThreadQueue<T> {
  void post(T message);
  void start();
}
