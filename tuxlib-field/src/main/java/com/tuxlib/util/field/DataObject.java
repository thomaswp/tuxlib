package com.tuxlib.util.field;

import java.util.HashMap;

import com.tuxlib.util.field.FieldData.ParseDataException;

public interface DataObject {

	public static abstract class Constructor {
		private static HashMap<String, Constructor> constructorMap = 
				new HashMap<String, Constructor>();
		private static HashMap<String, String> classMap = 
				new HashMap<String, String>();
		
		
		public abstract DataObject construct();
		
		public static void register(Class<?> clazz, Constructor constructor) {
			register(clazz, getSimpleName(clazz), constructor);
		}
		
		public static void register(Class<?> clazz, String key, Constructor constructor) {
			constructorMap.put(key, constructor);
			String className = clazz.getName();
			constructorMap.put(className, constructor);
			classMap.put(className, key);
		}
		
		private static String getSimpleName(Class<?> clazz) {
			String name = clazz.getName();
			int index = name.lastIndexOf(".");
			index = Math.max(index, name.lastIndexOf("$"));
			if (index >= 0) name = name.substring(index + 1, name.length());
			return name;
		}
		
		public static DataObject construct(Class<?> clazz) throws ParseDataException {
			return construct(clazz.getName());
		}
		
		public static DataObject construct(String key) throws ParseDataException {
			Constructor constructor = constructorMap.get(key);
			if (constructor == null) throw new ParseDataException("No constructor for type: " + key);
			return constructor.construct();
		}
		
		public static String className(Class<?> clazz) throws ParseDataException {
			if (clazz == null) return "null";
			return className(clazz.getName());
		}
		
		public static String className(String clazz) throws ParseDataException {
			if (clazz == null) return "null";
			String name = classMap.get(clazz);
			if (name == null) return clazz;
			return name;
		}
		
		public Constructor register(Class<?> clazz, String key) {
			register(clazz, key, this);
			return this;
		}
		
		public Constructor register(Class<?> clazz) {
			register(clazz, this);
			return this;
		}
		
		public void load() { }
	}
	
	void addFields(FieldData fields) throws ParseDataException, NumberFormatException;
}
