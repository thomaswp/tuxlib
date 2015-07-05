package com.tuxlib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import playn.core.PlayN;
import playn.core.util.Clock;

public abstract class Updater {

	private final static List<Updater> updaters = new ArrayList<Updater>();
	private final static List<Updater> toRemove = new ArrayList<Updater>();
	private final static HashMap<Object, HashMap<String, Updater>> uniqueMap = new HashMap<Object, HashMap<String,Updater>>();
	
	private Object tag;
	private String type;
	private final List<Runnable> callbacks = new ArrayList<Runnable>();
	
	public void add() {
		if (tag != null) {
			if (!uniqueMap.containsKey(tag)) {
				uniqueMap.put(tag, new HashMap<String, Updater>());
			}
			HashMap<String, Updater> objectMap = uniqueMap.get(tag);
			if (objectMap.containsKey(type)) {
				toRemove.add(objectMap.get(type));
			}
			objectMap.put(type, this);
		}
		updaters.add(this);
	}
	
	public Updater unique(Object tag) {
		return unique(tag, "");
	}
	
	public Updater unique(Object tag, String type) {
		this.tag = tag;
		this.type = type;
		return this;
	}
	
	public Updater addCallback(Runnable callback) {
		callbacks.add(callback);
		return this;
	}
	
	protected abstract boolean update(Clock clock);

	public static void paint(Clock clock) {
		while (toRemove.size() > 0) {
			updaters.remove(toRemove.remove(0));
		}
		for (int i = 0; i < updaters.size(); i++) {
			Updater updater = updaters.get(i);
			if (toRemove.contains(updater)) continue;
			if (!updater.update(clock)) {
				updaters.remove(i--);
				if (updater.tag != null) {
					HashMap<String,Updater> map = uniqueMap.get(updater.tag);
					if (map.get(updater.type) == updater) {
						map.remove(updater.type);
					}
				}
				for (Runnable callback : updater.callbacks) {
					PlayN.platform().invokeLater(callback);
				}
			}
		}
	}

	public static void clear() {
		updaters.clear();
		toRemove.clear();
		uniqueMap.clear();
	}
}
