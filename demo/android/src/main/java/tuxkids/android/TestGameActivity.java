package tuxkids.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import tuxkids.core.TestGame;

public class TestGameActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new TestGame());
  }
}
