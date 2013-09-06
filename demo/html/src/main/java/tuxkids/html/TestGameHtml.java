package tuxkids.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;
import tuxkids.core.TestGame;

public class TestGameHtml extends HtmlGame {

	@Override
	public void start() {
		HtmlPlatform.Config config = new HtmlPlatform.Config();
		// use config to customize the HTML platform, if needed
		HtmlPlatform platform = HtmlPlatform.register(config);
		platform.assets().setPathPrefix("tuxlibtest/");
		PlayN.run(new TestGame());
	}
}
