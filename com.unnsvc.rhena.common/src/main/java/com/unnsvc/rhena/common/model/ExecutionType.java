
package com.unnsvc.rhena.common.model;

public enum ExecutionType {

	DELIVERABLE, FRAMEWORK, TEST, INTEGRATION, PROTOTYPE;

	ExecutionType() {

	}

	public String toLabel() {

		return toString().toLowerCase();
	}
}
