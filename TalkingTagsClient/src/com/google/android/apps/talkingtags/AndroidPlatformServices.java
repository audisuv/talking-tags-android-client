package com.google.android.apps.talkingtags;

import com.google.android.apps.talkingtags.activities.TagActivity;

import android.content.Context;
import android.content.Intent;

/**
 * Control implementation of {@link PlatformServices} that uses
 * Android api.
 * 
 * @author adamconnors
 */
public class AndroidPlatformServices implements PlatformServices {
  
  private Context ctx;
  
  public AndroidPlatformServices(Context ctx) {
    this.ctx = ctx;
  }
  
  @Override
  public void startRfidListeningService() {
    Intent start = new Intent(ctx, RfidListeningService.class);
    start.setAction(RfidListeningService.ACTION_START);
    ctx.startService(start);
  }

  @Override
  public void stopRfidListeningService() {
    Intent start = new Intent(ctx, RfidListeningService.class);
    start.setAction(RfidListeningService.ACTION_STOP);
    ctx.startService(start);
  }

  @Override
  public void readTag(String nearbyTagData) {
    Intent read = new Intent(ctx, TagActivity.class);
    read.putExtra(TagActivity.EXTRA_ID, nearbyTagData);
    read.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    ctx.startActivity(read);
  }
}
