package com.tuxlib.util;

public abstract class Formatter {

	private final static String FORMAT_CHARS = "+-.";
	
	private static Formatter instance;
	
	public static void setFormatter(Formatter formatter) {
		Formatter.instance = formatter;
	}
	
	public abstract String doFormat(String format, Object... args);
	
	public static String format(String format, Object... args) {
		if (instance != null) return instance.doFormat(format, args);
		
		String buffer = "";
		boolean formatting = false;
		int index = 0;
		for (int i = 0; i < format.length(); i++) {
			char c = format.charAt(i);
			if (c == '%') {
				if (formatting && format.charAt(i - 1) == '%') {
					formatting = false;
					buffer += c;
					continue;
				} else if (!formatting) {
					formatting = true;
					continue;
				}
			}
			
			if (formatting) {
				if (c == '%') {
					buffer += args[index++];
				} else if (!Character.isLetterOrDigit(c) && FORMAT_CHARS.indexOf(c) == -1) {
					formatting = false;
					buffer += args[index++];
					buffer += c;
				} 
			} else {
				buffer += c;
			}	
		}
		if (formatting) buffer += args[index++];
		
		return buffer;
	}

}
