
package com.unnsvc.rhena.common.exceptions;

public class NotExistsException extends RhenaException {

	private static final long serialVersionUID = 1L;

	public NotExistsException(Throwable t) {

		super(t);
	}

	public NotExistsException(String message) {
		
		super(message);
	}

}
