
package com.unnsvc.rhena.objectserver.ng.messages;

public class ExceptionResponse implements Response {

	private static final long serialVersionUID = 1L;
	private Throwable throwable;

	public ExceptionResponse(Throwable throwable) {

		this.throwable = throwable;
	}
}
