package com.google.android.apps.talkingtags;

import java.util.ArrayList;
import java.util.List;

public class FakeRfidListener implements RfidListener {

  private RfidListener.State state = RfidListener.State.READY;
  
  @Override
  public State connect() {
    state = State.CONNECTED;
    return state;
  }

  @Override
  public State getState() {
    return state;
  }

  @Override
  public List<String> ping() {
    ArrayList<String> tags = new ArrayList<String>();
    tags.add("this-is-just-a-test");
    return tags;
  }

  @Override
  public void reset() {
    state = State.READY;
  }
}
