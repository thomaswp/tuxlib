package com.tuxlib.util.field;

import java.util.List;

public abstract class OrderedFieldData implements FieldData {

	@Override
	public int add(int x, String field) throws ParseDataException,
			NumberFormatException {
		return add(x);
	}

	@Override
	public long add(long x, String field) throws ParseDataException,
			NumberFormatException {
		return add(x);
	}

	@Override
	public short add(short x, String field) throws ParseDataException,
			NumberFormatException {
		return add(x);
	}

	@Override
	public float add(float x, String field) throws ParseDataException,
			NumberFormatException {
		return add(x);
	}

	@Override
	public double add(double x, String field) throws ParseDataException,
			NumberFormatException {
		return add(x);
	}

	@Override
	public byte add(byte x, String field) throws ParseDataException,
			NumberFormatException {
		return add(x);
	}

	@Override
	public char add(char x, String field) throws ParseDataException {
		return add(x);
	}

	@Override
	public boolean add(boolean x, String field) throws ParseDataException {
		return add(x);
	}

	@Override
	public String add(String x, String field) throws ParseDataException {
		return add(x);
	}

	@Override
	public <T extends DataObject> T addCast(T x, Class<? extends T> clazz,
			String field) throws ParseDataException, NumberFormatException {
		return addCast(x, clazz);
	}

	@Override
	public <T extends DataObject> T addCast(T x, String clazz, String field)
			throws ParseDataException, NumberFormatException {
		return addCast(x, clazz);
	}
	
	@Override
	public <T extends DataObject> T add(T x, String field)
			throws ParseDataException, NumberFormatException {
		return add(x);
	}

	@Override
	public boolean[] addArray(boolean[] x, String field)
			throws NumberFormatException, ParseDataException {
		return addArray(x);
	}

	@Override
	public int[] addArray(int[] x, String field) throws NumberFormatException,
			ParseDataException {
		return addArray(x);
	}

	@Override
	public String[] addArray(String[] x, String field)
			throws NumberFormatException, ParseDataException {
		return addArray(x);
	}
	
	@Override
	public Object addArray(Object x, PrimitiveArrayIO io, String field)
			throws NumberFormatException, ParseDataException {
		return addArray(x, io);
	}

	@Override
	public int[][] add2DArray(int[][] x, String field)
			throws NumberFormatException, ParseDataException {
		return add2DArray(x);
	}

	@Override
	public <T extends DataObject> T[] addArray(T[] x, String field)
			throws NumberFormatException, ParseDataException {
		return addArray(x);
	}

	@Override
	public <T extends DataObject, L extends List<T>> L addList(L x, String field)
			throws NumberFormatException, ParseDataException {
		return addList(x);
	}

	@Override
	public <T extends DataObject, L extends List<T>> L addList(L x,
			Class<? extends T> clazz, String field)
			throws NumberFormatException, ParseDataException {
		return addList(x, clazz);
	}

	@Override
	public Object addList(Object x, ListIO io, String field)
			throws NumberFormatException, ParseDataException {
		return addList(x, io);
	}
	
	@Override
	public List<Integer> addIntList(List<Integer> x, String field)
			throws NumberFormatException, ParseDataException {
		return addIntList(x);
	}

	@Override
	public List<String> addStringList(List<String> x, String field)
			throws NumberFormatException, ParseDataException {
		return addStringList(x);
	}

	@Override
	public List<Boolean> addBooleanList(List<Boolean> x, String field)
			throws NumberFormatException, ParseDataException {
		return addBooleanList(x);
	}
	
	@Override
	public <T extends Enum<T>> T addEnum(Enum<T> x, Enum<T>[] values, String field) throws ParseDataException {
		return addEnum(x, values);
	}
	
	@Override
	public <T extends Enum<T>> T addEnum(Enum<T> x, Enum<T>[] values) throws ParseDataException {
		return addEnumAsInt(this, x, values, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> T addEnumAsInt(FieldData fields, Enum<T> x, Enum<T>[] values, String field) throws ParseDataException {
		int ordinal = fields.add(x == null ? -1 : x.ordinal(), field);
		if (ordinal < 0 || ordinal >= values.length) return null;
		return (T) values[ordinal];
	}
	
	protected static void error(String message, Exception exception) {
		System.err.println(message);
		exception.printStackTrace();
	}
}
