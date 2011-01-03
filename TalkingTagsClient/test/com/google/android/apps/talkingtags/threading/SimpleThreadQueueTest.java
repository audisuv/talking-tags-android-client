/**
 * 
 */
package com.google.android.apps.talkingtags.threading;

import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * @author adamconnors
 */
public class SimpleThreadQueueTest extends TestCase {

  private ArrayList<String> executedMessages = new ArrayList<String>();
  
  private ThreadEndpoint<String> endpoint = new ThreadEndpoint<String>() {
    @Override
    public void execute(String msg) {
      executedMessages.add(msg);
    }
  };

  public void testThreadQueue() throws Exception {
    SimpleThreadQueue<String> queue = new SimpleThreadQueue<String>("test", endpoint);

    // These will queue up until the queue thread is started.
    queue.post("test1");
    queue.post("test2");
    queue.post("test3");
    
    // So this message will appear first.
    executedMessages.add("main1");
    
    // Start msg queue.
    queue.start();
    
    queue.post("test4");
    queue.post("test5");
    
    queue.waitUntilEmpty();
    
    queue.post("test6");
    queue.post("test7");
    
    queue.waitUntilEmpty();
    
    assertEquals("[main1, test1, test2, test3, test4, test5, test6, test7]", 
        executedMessages.toString());
  }
}
