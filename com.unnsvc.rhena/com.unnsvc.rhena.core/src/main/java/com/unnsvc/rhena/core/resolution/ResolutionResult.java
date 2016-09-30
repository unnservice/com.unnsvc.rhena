
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.core.model.RhenaNode;

public abstract class ResolutionResult {

	private Status status;
	private RhenaNode rhenaNode;
	private String message;

	public ResolutionResult(String message) {
		
		this.message = message;
		this.status = Status.SUCCESS;
	}
	
	public ResolutionResult(Status status, String message) {

		this.status = status;
		this.message = message;
	}

	public ResolutionResult(RhenaNode rhenaNode) {

		this.rhenaNode = rhenaNode;
		this.status = Status.SUCCESS;
	}

	public Status getStatus() {

		return status;
	}

	public RhenaNode getRhenaNode() {

		return rhenaNode;
	}

	public String getMessage() {

		return message;
	}

	public boolean isSuccess() {

		return status == Status.SUCCESS;
	}

	public static enum Status {

		SUCCESS, FAILURE;
	}
}
