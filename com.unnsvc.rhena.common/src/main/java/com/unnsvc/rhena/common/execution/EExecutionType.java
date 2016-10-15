
package com.unnsvc.rhena.common.execution;

public enum EExecutionType {

	MODEL, DELIVERABLE(MODEL), FRAMEWORK(MODEL), TEST(DELIVERABLE, FRAMEWORK), INTEGRATION(TEST), PROTOTYPE(TEST);

	private EExecutionType[] depends;

	EExecutionType(EExecutionType... depends) {

		this.depends = depends;
	}

	EExecutionType() {

		this.depends = new EExecutionType[] {};
	}

	public boolean isA(EExecutionType other) {

		if (other == this) {
			return true;
		}

		for (EExecutionType d : depends) {
			if (d == other) {
				return true;
			}
		}

		return false;
	}

	public String literal() {

		return toString().toLowerCase();
	}

}
