package tuxkids.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;
import tripleplay.util.Colors;
import tuxkids.tuxlib.CanvasUtils;
import tuxkids.tuxlib.ImageSaver;
import tuxkids.tuxlib.widget.Button;

public class TestGame extends Game.Default {

  public TestGame() {
    super(33); // call update every 33ms (30 times per second)
  }

  @Override
  public void init() {
    // create and add background image layer
    Image bgImage = assets().getImage("images/bg.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    graphics().rootLayer().add(bgLayer);

    CanvasImage image = CanvasUtils.createCircle(100, Colors.GREEN, 1, Colors.BLACK);
    ImageSaver.save(image, "test.png");
    Button button = new Button(image, true);
    button.setPosition(100, 100);
    graphics().rootLayer().add(button.layerAddable());
    button.layerAddable().setDepth(1);
  }

  @Override
  public void update(int delta) {
  }

  @Override
  public void paint(float alpha) {
    // the background automatically paints itself, so no need to do anything here!
  }
}
