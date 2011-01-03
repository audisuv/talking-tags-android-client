package com.google.android.apps.talkingtags.threading;


/**
 * The endpoint (recipient) of this thread queue.
 * 
 * @author adamconnors
 */
public interface ThreadEndpoint<T> {
  void execute(T msg);
}