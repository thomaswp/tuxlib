package tuxkids.tuxlib.widget;

import playn.core.Color;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.Layer.HitTester;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import playn.core.util.Callback;
import pythagoras.f.Point;
import tripleplay.util.Colors;
import tuxkids.tuxlib.Audio;
import tuxkids.tuxlib.PlayNObject;
import tuxkids.tuxlib.layers.ImageLayerTintable;

/**
 * A clickable wrapper for an {@link ImageLayer} with some
 * utility methods. Buttons always have their origin at their center.
 */
public class Button extends PlayNObject {
	
	// default alpha for the button when it is unpressed
	public final static float DEFAULT_UNPRESSED_ALPHA = 0.5f;
	
	private final ImageLayerTintable imageLayer;
	private final boolean isCircle;
	
	private OnPressedListener onPressedListener;
	private OnReleasedListener onReleaseListener;
	private OnDragListener onDragListener;
	private float width, height;
	private boolean pressed;
	private int tint, tintPressed;
	private boolean enabled = true;
	private boolean hovering;
	
	// sound to play when the button is pressed
	protected String soundPath = null;
	

	public Layer layerAddable() {
		return imageLayer.layerAddable();
	}
	
	public ImageLayerTintable imageLayer() {
		return imageLayer;
	}
	
	public Image image() {
		return imageLayer.image();
	}
	
	public float x() {
		return imageLayer.tx();
	}

	public float y() {
		return imageLayer.ty();
	}

	public float width() {
		return width;
	}
	
	public float height() {
		return height;
	}
	
	public boolean isCircle() {
		return isCircle;
	}

	public boolean pressed() {
		return pressed;
	}
	
	public int tint() {
		return imageLayer.tint();
	}
	
	public boolean enabled() {
		return enabled;
	}
	
	public float left() {
		return x() - width / 2;
	}
	
	public float top() {
		return y() - height / 2;
	}
	
	public float right() {
		return x() + width / 2;
	}
	
	public float bottom() {
		return y() + height / 2;
	}
	
	public void setLeft(float left) {
		setX(left + width / 2);
	}
	
	public void setTop(float top) {
		setY(top + height / 2);
	}
	
	public void setRight(float right) {
		setX(right - width / 2);
	}
	
	public void setBottom(float bottom) {
		setY(bottom - height / 2);
	}
	
	public void setX(float x) {
		imageLayer.setTx(x);
	}
	
	public void setY(float y) {
		imageLayer.setTy(y);
	}
	
	public void setPosition(float x, float y) {
		imageLayer.setTranslation(x, y);
	}
	
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
		if (imageLayer.image() != null && imageLayer.image().isReady()) {
			adjustScale();
		}
	}
	
	public void setImage(Image image) {
		imageLayer.setImage(image);
		imageLayer.setVisible(false);
		if (image != null) {
			imageLayer.image().addCallback(new Callback<Image>() {
				@Override
				public void onSuccess(Image result) {
					imageLayer.setVisible(true);
					imageLayer.setOrigin(result.width() / 2, result.height() / 2);
					adjustScale();
				}
	
				@Override
				public void onFailure(Throwable cause) {
					cause.printStackTrace();
				}
			});
		}
	}
	
	public void setOnPressListener(OnPressedListener onPressedListener) {
		this.onPressedListener = onPressedListener;
	}
	
	public void setOnReleasedListener(OnReleasedListener onReleasedListener) {
		this.onReleaseListener = onReleasedListener;
	}
	
	public void setOnDragListener(OnDragListener onDragListener) {
		this.onDragListener = onDragListener;
	}
	
	public void setTint(int tint) {
		setTint(tint, DEFAULT_UNPRESSED_ALPHA);
	}
	
	/** Sets this button's tint to the given color, and to the given alpha when unpressed. */
	public void setTint(int tint, float alphaUnpressed) {
		setTint(Color.withAlpha(tint, (int)(255 * alphaUnpressed)), tint);
	}
	
	/** Sets this button's tint to the given pressed and unpressed colors. */
	public void setTint(int tintUnpressed, int tintPressed) {
		this.tint = tintUnpressed;
		this.tintPressed = tintPressed;
		refreshTint();
	}
	
	/** Sets whether this button is enabled or not. Disabled buttons
	 *  have a darker color and cannot be pressed. */
	public void setEnabled(boolean enabled) {
		if (this.enabled == enabled) return;
		this.enabled = enabled;
		refreshTint();
	}
	
	public void setSoundPath(String soundPath) {
		this.soundPath = soundPath;
	}
	
	/** See {@link Button#Button(Image, float, float, boolean)} */
	public Button(String imagePath, float width, float height, boolean isCircle) {
		this(assets().getImage(imagePath), width, height, isCircle);
	}
	
	/** See {@link Button#Button(Image, float, float, boolean)} */
	public Button(Image image, boolean isCircle) {
		this(image, image == null ? 0 : image.width(), image == null ? 0 : image.height(), isCircle);
	}

	/**
	 * Creates a Button with the given Image, width and height, and whether the button
	 * is a circle or not. Circular buttons only respond to click within a radius
	 * of 1/2 their width. 
	 */
	public Button(Image image, float width, float height, boolean isCircle) {
		this.width = width;
		this.height = height;
		this.isCircle = isCircle;
		imageLayer = new ImageLayerTintable();
		setImage(image);
		imageLayer.addListener(new PointerListener());
		setTint(Colors.WHITE, DEFAULT_UNPRESSED_ALPHA);
		if (isCircle) {
			imageLayer.setHitTester(new HitTester() {
				@Override
				public Layer hitTest(Layer layer, Point p) {
					if (imageLayer.image() == null) return null;
					if (p.distance(imageLayer.image().width() / 2, 
							imageLayer.image().height() / 2) < 
							imageLayer.image().width() / 2) return layer;
					return null;
				}
			});
		}
	}
	
	private void refreshTint() {
		int tint = pressed ? tintPressed : this.tint;
		if (!enabled) {
			tint = Colors.blend(tint, Color.withAlpha(Colors.BLACK, Color.alpha(tint)), 0.5f);
		} else if (hovering) {
			tint = Color.withAlpha(tint, 255 - (255 - Color.alpha(tint)) / 2);
		}
		imageLayer.setTint(tint);
	}
	
	public void destroy() {
		imageLayer.destroy();
	}

	/** 
	 * Returns true if the given coordinates (relative to this Button's parent layer) 
	 * are inside the Button. Circular buttons will be hit if the click is within a 
	 * radius of 1/2 the button's width. 
	 */
	public boolean hit(float x, float y) {
		if (isCircle) {
			return hitCircle(x, y);
		} else {
			return hitRectangle(x, y);
		}
	}
	
	protected boolean hitRectangle(float x, float y) {
		return Math.abs(x - x()) < width / 2 && Math.abs(y - y()) < height / 2;
	}
	
	protected boolean hitCircle(float x, float y) {
		return distance(x(), y(), x, y) < width / 2;
	}
	
	private void adjustScale() {
		imageLayer.setScale(width / imageLayer.image().width(), 
				height / imageLayer.image().height());
	}
	private class PointerListener implements Listener {
		
		@Override
		public void onPointerStart(Event event) {
			if (!enabled || !insideLocal(event)) return;
			if (soundPath != null) Audio.se().play(soundPath);
			pressed = true;
			refreshTint();
			if (onPressedListener != null) onPressedListener.onPress(event);
		}

		@Override
		public void onPointerEnd(Event event) {
			if (!enabled || !pressed) return;
			pressed = false;
			refreshTint();
			if (onReleaseListener != null) onReleaseListener.onRelease(event, insideLocal(event));
		}
		
		// can't use the hit() method because these are local coordinates
		// and there's no way of knowing if the global coordinates are
		// aligned with the button's parent Layer
		private boolean insideLocal(Event event) {
			float dw = image().width() / 2;
			float dh = image().height() / 2;
			if (isCircle) {
				return distance(event.localX(), event.localY(), dw, dh) <= dw;
			} else {
				return Math.abs(event.localX() - dw) <= dw &&
						Math.abs(event.localY() - dh) <= dh;
			}
		}

		@Override
		public void onPointerDrag(Event event) {
			if (!enabled) return; 
			if (onDragListener != null) onDragListener.onDrag(event);
		}

		@Override
		public void onPointerCancel(Event event) {
			pressed = false;
			refreshTint();
		}
	}
	
	public interface OnReleasedListener {
		public void onRelease(Event event, boolean inButton);
	}
	
	public interface OnPressedListener {
		public void onPress(Event event);
	}
	
	public interface OnDragListener {
		public void onDrag(Event event);
	}

	/** Sets this button's sound to no sound */
	public void setNoSound() {
		setSoundPath(null);
	}
}
