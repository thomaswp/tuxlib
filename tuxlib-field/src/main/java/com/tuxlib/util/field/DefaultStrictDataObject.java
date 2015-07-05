package com.tuxlib.util.field;

import com.tuxlib.util.field.FieldData.ParseDataException;

public abstract class DefaultStrictDataObject implements StrictDataObject {

	@Override
	public final void addFields(FieldData fields) throws ParseDataException,
			NumberFormatException {
		addFields((StrictFieldData) fields);
	}
	
	public abstract void addFields(StrictFieldData fields) throws ParseDataException, NumberFormatException;
}
