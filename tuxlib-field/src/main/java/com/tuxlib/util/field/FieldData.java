package com.tuxlib.util.field;

import java.util.List;

public interface FieldData extends StrictFieldData {
	
	public static class ParseDataException extends Exception {
		private static final long serialVersionUID = 1L;
		
		public ParseDataException(String string) {
			super(string);
		}
		
		public ParseDataException() {
			super();
		}
	}
	
	public boolean writeMode();
	
	public boolean readMode();
	
	public int add(int x) throws ParseDataException, NumberFormatException;
	
	public long add(long x) throws ParseDataException, NumberFormatException;
	
	public short add(short x) throws ParseDataException, NumberFormatException;
	
	public float add(float x) throws ParseDataException, NumberFormatException;
	
	public double add(double x) throws ParseDataException, NumberFormatException;
	
	public byte add(byte x) throws ParseDataException, NumberFormatException;
	
	public char add(char x) throws ParseDataException;
	
	public boolean add(boolean x) throws ParseDataException;
	
	public String add(String x) throws ParseDataException;
	
	public <T extends Enum<T>> T addEnum(Enum<T> x, Enum<T>[] values) throws ParseDataException;

	public <T extends DataObject> T add(T x) throws ParseDataException, NumberFormatException;
	
	public <T extends DataObject> T addCast(T x, Class<? extends T> clazz) throws ParseDataException, NumberFormatException;
	public <T extends DataObject> T addCast(T x, String clazz) throws ParseDataException, NumberFormatException;

	public boolean[] addArray(boolean[] x) throws NumberFormatException, ParseDataException;
	
	public int[] addArray(int[] x) throws NumberFormatException, ParseDataException;
	
	public String[] addArray(String[] x) throws NumberFormatException, ParseDataException;
	
	public Object addArray(Object x, PrimitiveArrayIO io) throws NumberFormatException, ParseDataException;
	
	public int[][] add2DArray(int[][] x) throws NumberFormatException, ParseDataException;
	
	public <T extends DataObject> T[] addArray(T[] x) throws NumberFormatException, ParseDataException;
	
	public <T extends DataObject, L extends List<T>> L addList(L x) 
			throws NumberFormatException, ParseDataException; 
	
	public <T extends DataObject, L extends List<T>> L addList(L x, Class<? extends T> clazz) 
			throws NumberFormatException, ParseDataException;
	
	public Object addList(Object x, ListIO io) throws NumberFormatException, ParseDataException;
	
	public List<Integer> addIntList(List<Integer> x) throws NumberFormatException, ParseDataException;

	public List<String> addStringList(List<String> x) throws NumberFormatException, ParseDataException;
	
	public List<Boolean> addBooleanList(List<Boolean> x) throws NumberFormatException, ParseDataException;
}
