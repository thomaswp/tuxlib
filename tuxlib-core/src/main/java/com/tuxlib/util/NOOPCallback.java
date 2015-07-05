package com.tuxlib.util;

import playn.core.util.Callback;

public class NOOPCallback<T> implements Callback<T> {

	@Override
	public void onSuccess(T result) { }

	@Override
	public void onFailure(Throwable cause) { }

}
