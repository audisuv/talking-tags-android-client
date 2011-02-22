package com.google.android.apps.talkingtags;

/**
 * Some static configuration.
 * 
 * @author adamconnors
 */
public class Config {
  public static final String BASE_SERVER_PATH = "https://sites.google.com/site/talkingtags/home/";
  public static final String COLLECTIONS_RESOURCE = "available-collections.txt";
  public static final String SERVER_SUFFIX = "?attredirects=0&d=1";
  
  private static final boolean IS_DEMO_MODE = true;
  
  private static boolean isTest = false;
  
  public static void log(String msg) {
    if (isTest) {
      System.out.println(msg);
    } else {
      android.util.Log.d("tt", msg);
    }
  }
  
  public static void setTest(boolean b) {
    isTest = b;
  }

  public static boolean isDemoMode() {
    return IS_DEMO_MODE;
  }
}
