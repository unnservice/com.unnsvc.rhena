
package com.unnsvc.rhena.objectserver;

public class ObjectServerException extends Exception {

	private static final long serialVersionUID = 1L;

	public ObjectServerException(Throwable t) {

		super(t);
	}

	public ObjectServerException(String message, Throwable t) {

		super(message, t);
	}

}
