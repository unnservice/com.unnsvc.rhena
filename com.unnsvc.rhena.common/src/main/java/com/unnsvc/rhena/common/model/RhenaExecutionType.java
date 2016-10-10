
package com.unnsvc.rhena.common.model;

public enum RhenaExecutionType {

	COMPILE, TEST(RhenaExecutionType.COMPILE), ITEST(RhenaExecutionType.TEST);

	private RhenaExecutionType dependency;

	RhenaExecutionType(RhenaExecutionType dependency) {

		this.dependency = dependency;
	}

	RhenaExecutionType() {

	}

	public RhenaExecutionType getDependency() {

		return dependency;
	}

	public String toLabel() {

		return toString().toLowerCase();
	}
}
