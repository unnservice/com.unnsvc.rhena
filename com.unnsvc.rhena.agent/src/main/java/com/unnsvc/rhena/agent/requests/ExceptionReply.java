
package com.unnsvc.rhena.agent.requests;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.IObjectReply;

public class ExceptionReply implements IObjectReply {

	private static final long serialVersionUID = 1L;
	private RhenaException exception;

	public ExceptionReply(RhenaException exception) {

		this.exception = exception;
	}

	public RhenaException getException() {

		return exception;
	}
}
