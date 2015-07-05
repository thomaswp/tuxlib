package com.tuxlib.util.field;

import com.tuxlib.util.json.Json.Array;


public abstract class PrimitiveArrayIO extends PrimitiveArrayHash {
	public abstract String read(Object x, int index);
	public abstract void set(Object x, int index, String value);
	public abstract Object create(int length);
	public abstract Object readObject(Object x, int i);
	public abstract void set(Object x, Array a, int i);
	
	public final static PrimitiveArrayIO intIO = new PrimitiveArrayIO() {
		
		private int[] cast(Object x) {
			return (int[]) x;
		}
		
		@Override
		public void set(Object x, int index, String value) {
			cast(x)[index] = Integer.parseInt(value);
		}

		@Override
		public void set(Object x, Array a, int index) {
			cast(x)[index] = a.getInt(index);
		}
		
		@Override
		public String read(Object x, int index) {
			return String.valueOf(cast(x)[index]);
		}
		
		@Override
		public int length(Object x) {
			return cast(x).length;
		}
		
		@Override
		public Object create(int length) {
			return new int[length];
		}

		@Override
		public boolean areEqual(Object x1, Object x2, int index) {
			return cast(x1)[index] == cast(x2)[index];
		}

		@Override
		public int hash(Object x, int index) {
			return cast(x)[index];
		}

		@Override
		public Object readObject(Object x, int i) {
			return cast(x)[i];
		}
	};
	
	public final static PrimitiveArrayIO booleanIO = new PrimitiveArrayIO() {
		
		private boolean[] cast(Object x) {
			return (boolean[]) x;
		}
		
		@Override
		public void set(Object x, int index, String value) {
			cast(x)[index] = Boolean.parseBoolean(value);
		}

		@Override
		public void set(Object x, Array a, int index) {
			cast(x)[index] = a.getBoolean(index);
		}
		
		@Override
		public String read(Object x, int index) {
			return String.valueOf(cast(x)[index]);
		}
		
		@Override
		public int length(Object x) {
			return cast(x).length;
		}
		
		@Override
		public Object create(int length) {
			return new boolean[length];
		}

		@Override
		public boolean areEqual(Object x1, Object x2, int index) {
			return cast(x1)[index] == cast(x2)[index];
		}
		
		@Override
		public int hash(Object x, int index) {
			return cast(x)[index] ? 1231 : 1237;
		}
		
		@Override
		public Object readObject(Object x, int i) {
			return cast(x)[i];
		}
	};
	
	public final static PrimitiveArrayIO stringIO = new PrimitiveArrayIO() {
		
		private String[] cast(Object x) {
			return (String[]) x;
		}
		
		@Override
		public void set(Object x, int index, String value) {
			cast(x)[index] = value;
		}

		@Override
		public void set(Object x, Array a, int index) {
			cast(x)[index] = a.getString(index);
		}
		
		@Override
		public String read(Object x, int index) {
			return cast(x)[index];
		}
		
		@Override
		public int length(Object x) {
			return cast(x).length;
		}
		
		@Override
		public Object create(int length) {
			return new String[length];
		}

		@Override
		public boolean areEqual(Object x1, Object x2, int index) {
			return cast(x1)[index].equals(cast(x2)[index]);
		}
		
		@Override
		public int hash(Object x, int index) {
			return cast(x)[index].hashCode();
		}
		
		@Override
		public Object readObject(Object x, int i) {
			return cast(x)[i];
		}
	};
}
