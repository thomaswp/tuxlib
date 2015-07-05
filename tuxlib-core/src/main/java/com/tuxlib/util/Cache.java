package com.tuxlib.util;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.log;

import java.util.HashMap;

import playn.core.Font;
import playn.core.Font.Style;
import playn.core.Image;
import playn.core.PlayN;
import playn.core.Sound;
import playn.core.TextFormat;
import playn.core.util.Callback;

/**
 * Class for caching various resources for reuse.
 */
public class Cache {

	private final static HashMap<Key, Image> imageMap = new HashMap<Cache.Key, Image>();
	private final static HashMap<Key, Sound> musicMap = new HashMap<Cache.Key, Sound>();
	private final static HashMap<Key, String> textMap = new HashMap<Cache.Key, String>();
	private final static HashMap<Key, Font> fontMap = new HashMap<Cache.Key, Font>();
	
	private static int loading = 0; 
	
	private final static FontKey fontKey = new FontKey(); 
	
	public static void clear() {
		imageMap.clear();
		fontMap.clear();
		musicMap.clear();
		textMap.clear();
		setLoading(0);
	}
	
	private synchronized static void incremementLoading(int by) {
		Cache.loading += by;
	}
	
	private synchronized static void setLoading(int loading) {
		Cache.loading = loading;
	}
	
	public static int loading() {
		return loading;
	}
	
	private static class LoadingCallback<T> implements Callback<T> {

		public LoadingCallback() {
			incremementLoading(1);
		}
		
		@Override
		public void onSuccess(T result) {
			incremementLoading(-1);
		}

		@Override
		public void onFailure(Throwable cause) {
			log().error("Failed to load asset", cause);
			incremementLoading(-1);
		}
		
	}
	
	public static Image getImage(Key key) {
		return imageMap.get(key);
	}
	
	public static Image putImage(Key key, Image image) {
		imageMap.put(key.copy(), image);
		return image;
	}
	
	public static Image getImage(String path) {
		StringKey key = new StringKey(path);
		Image image = getImage(key);
		if (image == null) {
			image = assets().getImage(path);
			image.addCallback(new LoadingCallback<Image>());
			putImage(key, image);
		}
		return image;
	}
	
	public static Sound getMusic(String path) {
		StringKey key = new StringKey(path);
		Sound sound = musicMap.get(key);
		if (sound == null) {
			System.out.println(path);
			sound = assets().getMusic(path);
			sound.addCallback(new LoadingCallback<Sound>());
			musicMap.put(key, sound);
		}
		return sound;
	}
	
	public static void getText(String path, final Callback<String> callback) {
		final StringKey key = new StringKey(path);
		String text = textMap.get(key);
		if (text == null) {
			assets().getText(path, new LoadingCallback<String>() {
				@Override
				public void onSuccess(String result) {
					super.onSuccess(result);
					textMap.put(key, result);
					if (callback != null) callback.onSuccess(result);
				}

				@Override
				public void onFailure(Throwable cause) {
					super.onFailure(cause);
					if (callback != null) callback.onFailure(cause);
				}
			});
		} else {
			callback.onSuccess(text);
		}
	}
	
	public static Font getFont(String name, Style style, float size) {
		Font font = fontMap.get(fontKey.set(name, style, size));
		if (font == null) {
			font = PlayN.graphics().createFont(name, style, size);
			fontMap.put(fontKey.copy(), font);
		}
		return font;
	}
	
	public static TextFormat createFormat(String fontName, float size) {
		return createFormat(fontName, Style.PLAIN, size);
	}
	
	public static TextFormat createFormat(String fontName, Style style, float size) {
		return new TextFormat().withFont(getFont(fontName, style, size));
	}
	
	/**
	 * Identifies a resource for reuse later. Should be able to copy iteself
	 * to a new instance if necessary.
	 */
	public static abstract class Key {
		public abstract Key copy();

		/**
		 * Creates a Key based on the supplied class and any Object.
		 * Note: Keys are compared using equals(), and so will
		 * the supplied Object.
		 */
		public static Key fromClass(Class<?> clazz, Object key) {
			return new ClassKey(clazz, key);
		}
	}
	
	private static class StringKey extends Key {

		private final String str;
		
		public StringKey(String str) {
			this.str = str;
		}
		
		@Override
		public Key copy() {
			return new StringKey(str);
		}
		
		
		@Override
		public int hashCode() {
			return str.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			return obj != null && obj instanceof StringKey && str.equals(((StringKey) obj).str);
		}
	}
	
	private static class ClassKey extends Key {

		private Class<?> clazz;
		private Object key;
		
		public ClassKey(Class<?> clazz, Object key) {
			this.clazz = clazz;
			this.key = key;
		}

		@Override
		public Key copy() {
			return new ClassKey(clazz, key);
		}
		
	}
	
	private static class FontKey extends Key {

		private String name;
		private Style style;
		private float size;
		
		public FontKey set(String name, Style style, float size) {
			this.name = name;
			this.style = style;
			this.size = size;
			return this;
		}

		@Override
		public Key copy() {
			return new FontKey().set(name, style, size);
		}
		
	}
}
