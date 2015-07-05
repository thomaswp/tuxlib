package com.tuxlib.util.field;

public abstract class PrimitiveArrayHash {
	public abstract boolean areEqual(Object x1, Object x2, int index);
	public abstract int hash(Object x, int index);
	public abstract int length(Object x);

}
