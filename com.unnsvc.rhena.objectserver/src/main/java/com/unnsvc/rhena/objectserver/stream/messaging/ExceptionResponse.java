
package com.unnsvc.rhena.objectserver.stream.messaging;

public class ExceptionResponse implements IResponse {

	private static final long serialVersionUID = 1L;
	private Throwable throwable;

	public ExceptionResponse(Throwable throwable) {

		this.throwable = throwable;
	}

}
