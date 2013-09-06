package tuxkids.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;
import tuxkids.core.TestGame;
import tuxkids.tuxlib.ImageSaver;

public class TestGameJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    // use config to customize the Java platform, if needed
    JavaPlatform.register(config);
    ImageSaver.saver = new JavaImageSaver();
    JavaIconSetter.setIcon("test.png", "test.png");
    PlayN.run(new TestGame());
  }
}
