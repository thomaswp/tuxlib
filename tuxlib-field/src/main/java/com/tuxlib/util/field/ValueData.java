package com.tuxlib.util.field;


abstract class ValueData extends OrderedFieldData {
	protected final DataObject dataObject;
	
	public ValueData(DataObject dataObject) {
		this.dataObject = dataObject;
	}
	
	protected abstract void reset();
	
	protected final static int prime = 31;
	
	protected static int hashArray(Object a, PrimitiveArrayHash io) {
		int length = io.length(a);
		int result = 1;
		for (int i = 0; i < length; i++) {
			result = prime * result + io.hash(a, i);
		}
		return result;
	}
	
	protected static boolean areArraysEqual(Object a1, Object a2, PrimitiveArrayHash io) {
		int length1 = io.length(a1);
		int length2 = io.length(a2);
		if (length1 != length2) return false;
		for (int i = 0; i < length1; i++) {
			if (!io.areEqual(a1, a2, i)) return false;
		}
		return true;
	}
	
	public final static PrimitiveArrayHash int2dIO = new PrimitiveArrayHash() {
		
		private int[][] cast(Object x) {
			return (int[][]) x;
		}

		@Override
		public int length(Object x) {
			return cast(x).length;
		}
		
		@Override
		public boolean areEqual(Object x1, Object x2, int index) {
			return areArraysEqual(cast(x1)[index], cast(x2)[index], PrimitiveArrayIO.intIO);
		}
		
		@Override
		public int hash(Object x, int index) {
			return hashArray(cast(x)[index], PrimitiveArrayIO.intIO);
		}
	};

	@Override
	public boolean writeMode() {
		return true;
	}

	@Override
	public boolean readMode() {
		return false;
	}
}
