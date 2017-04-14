
package com.unnsvc.rhena.common.exceptions;

public class NotFoundException extends RhenaException {

	private static final long serialVersionUID = 1L;

	public NotFoundException(Throwable t) {

		super(t.getMessage(), t);
	}

	public NotFoundException(String message) {
		
		super(message);
	}

}
