package com.tuxlib.util.layer;

import com.tuxlib.util.Updater;
import com.tuxlib.util.layer.LayerState.FloatProperty;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.util.Callback;
import playn.core.util.Clock;
import pythagoras.f.FloatMath;

public class LayerUtils {
	
	public static void centerLayer(final ImageLayer layer) {
		if (layer.image() != null) {
			layer.image().addCallback(new Callback<Image>() {
				@Override
				public void onSuccess(Image result) {
					layer.setOrigin(result.width() / 2, result.height() / 2);
				}

				@Override
				public void onFailure(Throwable cause) { }
			});
		}
	}
	
	public static void centerLayer(final ImageLayerLike layer) {
		if (layer.image() != null) {
			layer.image().addCallback(new Callback<Image>() {
				@Override
				public void onSuccess(Image result) {
					layer.setOrigin(result.width() / 2, result.height() / 2);
				}

				@Override
				public void onFailure(Throwable cause) { }
			});
		}
	}

	public static void lerpState(Layer layer, LayerState state, float factor) {
		lerpState(layer, state, factor, null);
	}
	
	public static void lerpState(Layer layer, LayerState state, float factor, Runnable callback) {
		lerpState(layer, state, factor, callback, "");
	}
	
	public static void lerpState(final Layer layer, final LayerState state, final float factor, final Runnable callback, String tag) {
		new Updater() {
			@Override
			protected boolean update(Clock clock) {
				float perc = 1 - FloatMath.pow(factor, clock.dt());
				if (!state.lerp(layer, perc)) {
					if (callback != null) callback.run();
					return false;
				}
				return true;
			}
		}.unique(layer, tag).add();
	}
	
	public static void fadeIn(final Layer layer, final float factor) {
		fadeIn(layer, factor, null);
	}
	
	public static void fadeIn(final Layer layer, final float factor, Runnable callback) {
		fade(1, layer, factor, callback);
	}
	
	public static void fadeOut(final Layer layer, final float factor) {
		fadeOut(layer, factor, null);
	}
	
	public static void fadeOut(final Layer layer, final float factor, Runnable callback) {
		fade(0, layer, factor, callback);
	}
	
	private static void fade(final float targetAlpha, final Layer layer, final float factor, final Runnable callback) {
		LayerState state = new LayerState().addFloatProperty(FloatProperty.Alpha, targetAlpha);
		lerpState(layer, state, factor, callback, "alpha");
	}

	public static void lerpScale(Layer layer, float targetScale, float factor) {
		lerpScale(layer, targetScale, factor, null);
	}
	
	public static void lerpScale(final Layer layer, final float targetScale, final float factor, final Runnable callback) {
		LayerState state = new LayerState().addFloatProperty(FloatProperty.ScaleX, targetScale)
				.addFloatProperty(FloatProperty.ScaleY, targetScale);
		lerpState(layer, state, factor, callback, "scale");
	}
	
	public static void lerpTint(Layer layer, int targetTint, float factor) {
		lerpTint(layer, targetTint, factor, null);
	}
	
	public static void lerpTint(final Layer layer, final int targetTint, final float factor, final Runnable callback) {
		LayerState state = new LayerState().addTint(targetTint);
		lerpState(layer, state, factor, callback, "tint");
	}
}
