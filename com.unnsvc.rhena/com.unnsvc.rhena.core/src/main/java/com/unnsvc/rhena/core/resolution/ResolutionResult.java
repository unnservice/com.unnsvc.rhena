package com.unnsvc.rhena.core.resolution;

public abstract class ResolutionResult {

	private Status status;

	public ResolutionResult(Status status) {

		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public static enum Status {

		SUCCESS, FAILURE;
	}
}
