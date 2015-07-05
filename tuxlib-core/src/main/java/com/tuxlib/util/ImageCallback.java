package com.tuxlib.util;

import static playn.core.PlayN.log;
import playn.core.Image;
import playn.core.util.Callback;

public abstract class ImageCallback implements Callback<Image> {
	@Override
	public void onFailure(Throwable cause) {
		log().error("Error loading image", cause);
	}
}
