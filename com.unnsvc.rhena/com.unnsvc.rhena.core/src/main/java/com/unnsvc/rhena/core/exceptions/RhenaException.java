package com.unnsvc.rhena.core.exceptions;

public class RhenaException extends Exception {

	public RhenaException(String message) {
		
		super(message);
	}

	public RhenaException(Throwable t) {
		
		super(t);
	}

	private static final long serialVersionUID = 1L;

	
}
