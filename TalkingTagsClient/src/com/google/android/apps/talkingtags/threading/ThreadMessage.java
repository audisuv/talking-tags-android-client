package com.google.android.apps.talkingtags.threading;

/**
 * Represents a generic message to be passed onto the non-UI threads.
 *
 * @author adamconnors
 *
 */
public interface ThreadMessage<T> {
  T getPayload();
}