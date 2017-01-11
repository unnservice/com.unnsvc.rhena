
package com.unnsvc.rhena.common.exceptions;

import java.rmi.RemoteException;

public class RhenaException extends RemoteException {

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
