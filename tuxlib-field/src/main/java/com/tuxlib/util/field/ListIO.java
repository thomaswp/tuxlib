package com.tuxlib.util.field;

public interface ListIO {
	public int length(Object x);
	public Object create(int length);
	public void set(Object x, int index, DataObject value);
	public DataObject get(Object x, int index);
	public void clear(Object x);
}
