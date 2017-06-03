
package com.unnsvc.rhena.common.exceptions;

public class RhenaException extends Exception {

	private static final long serialVersionUID = 1L;

	public RhenaException(String message) {

		super(message);
	}

	public RhenaException(Throwable t) {

		super(t.getMessage(), t);
	}

	public RhenaException(String message, Throwable t) {

		super(message, t);
	}

}
