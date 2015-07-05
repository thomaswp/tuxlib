package com.tuxlib.util;

import static playn.core.PlayN.log;

import java.util.HashMap;

import playn.core.PlayN;
import tripleplay.sound.Clip;
import tripleplay.sound.Loop;
import tripleplay.sound.Playable;
import tripleplay.sound.SoundBoard;

/**
 * Class for playing Audio. Contains a BG instance for background music
 * and an SE instance for sound effects. All methods are static.
 */
public abstract class Audio {
	
	protected final static String BG_VOLUME_KEY = "bg-volume";
	protected final static String SE_VOLUME_KEY = "se-volume";
	
	private static Audio bg = new BG(), se = new SE();
	
	public static Audio bg() {
		return bg;
	}
	
	public static Audio se() {
		return se;
	}
	
	// called from TuxBlockGame
	public static void update(int delta) {
		bg.updateInstance(delta);
		se.updateInstance(delta);
	}

	// called from TuxBlockGame
	public static void clear() {
		bg = new BG();
		se = new SE();
	}

	protected abstract Playable load(String path);
	protected abstract String volumeKey();
	public abstract void stop();
	
	protected final SoundBoard soundboard;
	// cache sound effects
	protected final HashMap<String, Playable> cache =
			new HashMap<String, Playable>();
	
	protected Playable lastPlayed;
	
	private Audio() {
		soundboard = new SoundBoard();
		//load previous volume
		String volume = PlayN.storage().getItem(volumeKey());
		if (volume != null) {
			try {
				setVolume(Float.parseFloat(volume));
			} catch (NumberFormatException e) {
				log().error("Error loading volume from storage", e);
			}
		} else {
			setVolume(0.5f);
		}
	}
	
	public float volume() {
		return soundboard.volume.get();
	}
	
	public void setVolume(float volume) {
		soundboard.volume.update(Math.min(Math.max(volume, 0), 1));
	}
	
	public void play(String path) {
		preload(path); // load the sound into the cache
		Playable playing = cache.get(path);
		playing.play();
		lastPlayed = playing;
	}
	
	public void play(String path, float volume) {
		play(path);
		lastPlayed.setVolume(volume);
	}

	private void updateInstance(int delta) {
		soundboard.update(delta);
	}
	
	public void preload(String path) {
		if (cache.containsKey(path)) return;
		cache.put(path, load(path));
	}

	public void stop(String path) {
		Playable p = cache.get(path);
		if (p != null) p.stop();
	}

	public boolean isPlaying(String path) {
		Playable p = cache.get(path);
		if (p != null) return p.isPlaying();
		return false;
	}

	public void restart() {
		if (lastPlayed != null) {
			lastPlayed.stop();
			lastPlayed.play();
		}
	}
	
	protected static class BG extends Audio {

		@Override
		protected Playable load(String path) {
			return soundboard.getLoop(path);
		}

		public void play(String path) {
			// fade out other background music before playing
			for (Playable p : cache.values()) {
				if (p.isPlaying()) {
					((Loop) p).stop();
					// TODO: Why isn't fadeout working?
//					((Loop) p).fadeOut(1000);
				}
				p.release();
			}
			cache.clear();
			super.play(path);
			((Loop) lastPlayed).fadeIn(1000);
		}
		
		@Override
		public void stop() {
			if (lastPlayed == null) return;
			((Loop) lastPlayed).stop();
			// TODO: Why isn't fadeout working?
//			((Loop) lastPlayed).fadeOut(1000);
		}
		
		@Override
		public void setVolume(float volume) {
			super.setVolume(Math.max(volume, 0.001f));
		}

		@Override
		protected String volumeKey() {
			return BG_VOLUME_KEY;
		}
	}
	
	protected static class SE extends Audio {
		
		@Override
		protected Playable load(String path) {
			Clip clip = soundboard.getClip(path);
			clip.preload();
			return clip;
		}
		
		public void play(String path) {
			preload(path);
			Playable playing = cache.get(path);
			// Only play the sound effect if it's not already playing
			if (playing != lastPlayed || lastPlayed == null || !lastPlayed.isPlaying()) {
				super.play(path);
			}
		}

		@Override
		public void stop() {
			
		}
		
		@Override
		protected String volumeKey() {
			return SE_VOLUME_KEY;
		}
	}
}
