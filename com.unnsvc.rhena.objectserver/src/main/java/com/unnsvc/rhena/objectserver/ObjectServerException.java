
package com.unnsvc.rhena.objectserver;

public class ObjectServerException extends Exception {

	private static final long serialVersionUID = 1L;

	public ObjectServerException(Throwable throwable) {

		super(throwable);
	}

	public ObjectServerException(String message) {

		super(message);
	}
}
