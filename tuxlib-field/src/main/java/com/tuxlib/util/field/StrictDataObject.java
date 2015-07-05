package com.tuxlib.util.field;

import com.tuxlib.util.field.FieldData.ParseDataException;


public interface StrictDataObject extends DataObject {

	void addFields(StrictFieldData fields) throws ParseDataException, NumberFormatException;
}
