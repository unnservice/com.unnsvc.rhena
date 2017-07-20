
package com.unnsvc.rhena.objectserver.messages;

public class ExceptionResponse implements IResponse {

	private static final long serialVersionUID = 1L;
	private Throwable throwable;

	public ExceptionResponse(Throwable throwable) {

		this.throwable = throwable;
	}

	public Throwable getThrowable() {

		return throwable;
	}
}
