package com.unnsvc.rhena.core.exceptions;

public class RhenaParserException extends RhenaException {

	private static final long serialVersionUID = 1L;

	public RhenaParserException(String message) {

		super(message);
	}

	public RhenaParserException(Throwable t) {

		super(t);
	}

	public RhenaParserException(Throwable t, String messagee) {
		
		super(t, message);
	}
}
