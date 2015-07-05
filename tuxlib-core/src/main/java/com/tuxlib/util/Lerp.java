package com.tuxlib.util;

import com.tuxlib.util.layer.LayerLike;

import playn.core.Layer;
import pythagoras.f.FloatMath;
import pythagoras.f.Vector;
import tripleplay.util.Colors;

public class Lerp {
	public static float lerp(float x0, float x1, float perc) {
		return x0 * (1 - perc) + x1 * perc;
	}
	
	public static float lerp(float x0, float x1, float perc, float snapDistance) {
		float x = x0 * (1 - perc) + x1 * perc;
		if (Math.abs(x - x1) < snapDistance) x = x1;
		return x;
	}
	
	public static void lerp(Vector v0, float x1, float y1, float perc) {
		v0.x = lerp(v0.x, x1, perc);
		v0.y = lerp(v0.y, y1, perc);
	}
	
	public static float lerpTime(float x0, float x1, float base, float dt) {
		float perc = 1 - FloatMath.pow(base, dt);
		return lerp(x0, x1, perc);
	}
	
	/** Linearly interpolates, but uses an elapsed time instead of a factor, 
	 * making the interpolation smoother if the frames aren't consistent. */
	public static float lerpTime(float x0, float x1, float base, float dt, float snapDistance) {
		float perc = 1 - FloatMath.pow(base, dt);
		return lerp(x0, x1, perc, snapDistance);
	}

	public static void lerpTime(Vector v0, int x1, int y1, float base, float dt) {
		v0.x = lerpTime(v0.x, x1, base, dt);
		v0.y = lerpTime(v0.y, y1, base, dt);
	}
	
	public static void lerpAlpha(LayerLike layer, float target, float base, float dt) {
		layer.setAlpha(lerpTime(layer.alpha(), target, base, dt, 0.01f));
	}
	
	public static void lerpAlpha(Layer layer, float target, float base, float dt) {
		layer.setAlpha(lerpTime(layer.alpha(), target, base, dt, 0.01f));
	}
	
	/** Returns x0, linearly shifted to x1 by maxShift, but not past x1 */
	public static float shiftTo(float x0, float x1, float maxShift) {
		float change = x1 - x0;
		change = Math.min(Math.abs(change), maxShift) * Math.signum(change);
		return x0 + change;
	}

	public static int lerpColor(int tint, int targetTint, float base, float dt) {
		float perc = FloatMath.pow(base, dt);
		return lerpColor(tint, targetTint, perc);
	}
	
	public static int lerpColor(int tint, int targetTint, float perc) {
		return Colors.blend(tint, targetTint, perc);
	}
}
