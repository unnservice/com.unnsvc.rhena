package com.unnsvc.rhena.core.resolution;

public class ResolutionFailure extends ResolutionResult {

	private String reason;

	public ResolutionFailure(String reason) {

		super(Status.FAILURE);
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
}
