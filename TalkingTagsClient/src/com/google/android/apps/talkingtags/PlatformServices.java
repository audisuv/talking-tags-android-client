/**
 * 
 */
package com.google.android.apps.talkingtags;

/**
 * Interface for interacting with platform services. Wrapped in an interface so it can be
 * mocked out in tests.
 * 
 * @author adamconnors
 */
public interface PlatformServices {
  void startRfidListeningService();
  void stopRfidListeningService();
}
