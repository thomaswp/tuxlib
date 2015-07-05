package com.tuxlib.util.layer;

import java.util.HashMap;

import com.tuxlib.util.Lerp;

import playn.core.Layer;

public class LayerState {
	public enum FloatProperty {
		TX, TY, OriginX, OriginY, Rotation, Alpha, ScaleX, ScaleY
	}

	
	private interface FloatAccessor {
		float get(Layer layer);
		void set(Layer layer, float value);
		float thresh();
	}
	
	private final static HashMap<FloatProperty, FloatAccessor> floatAccessors = 
			new HashMap<LayerState.FloatProperty, LayerState.FloatAccessor>();
	
	static {
		floatAccessors.put(FloatProperty.TX, new FloatAccessor() {
			@Override public void set(Layer layer, float value) { layer.setTx(value); }
			@Override public float get(Layer layer) { return layer.tx(); }
			@Override public float thresh() { return 1; }
		});
		floatAccessors.put(FloatProperty.TY, new FloatAccessor() {
			@Override public void set(Layer layer, float value) { layer.setTy(value); }
			@Override public float get(Layer layer) { return layer.ty(); }
			@Override public float thresh() { return 1; }
		});
		floatAccessors.put(FloatProperty.Rotation, new FloatAccessor() {
			@Override public void set(Layer layer, float value) { layer.setRotation(value); }
			@Override public float get(Layer layer) { return layer.rotation(); }
			@Override public float thresh() { return 1; }
		});
		floatAccessors.put(FloatProperty.Alpha, new FloatAccessor() {
			@Override public void set(Layer layer, float value) { layer.setAlpha(value); }
			@Override public float get(Layer layer) { return layer.alpha(); }
			@Override public float thresh() { return 0.03f; }
		});
		floatAccessors.put(FloatProperty.ScaleX, new FloatAccessor() {
			@Override public void set(Layer layer, float value) { layer.setScaleX(value); }
			@Override public float get(Layer layer) { return layer.scaleX(); }
			@Override public float thresh() { return 0.01f; }
		});
		floatAccessors.put(FloatProperty.ScaleY, new FloatAccessor() {
			@Override public void set(Layer layer, float value) { layer.setScaleY(value); }
			@Override public float get(Layer layer) { return layer.scaleY(); }
			@Override public float thresh() { return 0.01f; }
		});
		floatAccessors.put(FloatProperty.OriginX, new FloatAccessor() {
			@Override public void set(Layer layer, float value) { layer.setOrigin(value, layer.originY()); }
			@Override public float get(Layer layer) { return layer.originX(); }
			@Override public float thresh() { return 1f; }
		});
		floatAccessors.put(FloatProperty.OriginY, new FloatAccessor() {
			@Override public void set(Layer layer, float value) { layer.setOrigin(layer.originX(), value); }
			@Override public float get(Layer layer) { return layer.originY(); }
			@Override public float thresh() { return 1f; }
		});
	}
	
	private final HashMap<FloatProperty, Float> floatProperties = new HashMap<LayerState.FloatProperty, Float>();
	private Integer tint;
	
	public LayerState addFloatProperty(FloatProperty property, float value) {
		floatProperties.put(property, value);
		return this;
	}
	
	public LayerState addTint(int tint) {
		this.tint = tint;
		return this;
	}
	
	public boolean lerpTime(Layer layer, float dt, float base) {
		return lerp(layer, 1 - (float)Math.pow(base, dt));
	}
	
	public boolean lerp(Layer layer, float perc) {
		boolean updated = false;
		for (FloatProperty prop : floatProperties.keySet()) {
			float targetValue = floatProperties.get(prop);
			FloatAccessor accessor = floatAccessors.get(prop);
			float value = accessor.get(layer);
			if (thresh(targetValue, value, accessor.thresh())) {
				accessor.set(layer, targetValue);
			} else {
				updated = true;
				accessor.set(layer, Lerp.lerp(value, targetValue, perc));
			}
		}
		if (tint != null) updated |= lerpTint(layer, tint, perc);
		
		return updated;
	}
	
	public void set(Layer layer) {
		lerp(layer, 1);
	}

	private boolean thresh(float actual, float target, float thresh) {
		return Math.abs(actual - target) < thresh;
	}
	
	private boolean lerpTint(Layer layer, int tint, float perc) {
		if (layer.tint() == tint) return false;
		layer.setTint(Lerp.lerpColor(layer.tint(), tint, perc));
		return true;
	}
	
}
