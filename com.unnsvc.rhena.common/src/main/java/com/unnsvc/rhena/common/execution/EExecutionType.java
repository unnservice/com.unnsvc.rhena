
package com.unnsvc.rhena.common.execution;

public enum EExecutionType {

	/**
	 * Keep it simple in a flat model without hierarchies
	 */
	MODEL, MAIN, TEST, ITEST, PROTO;

	EExecutionType() {

	}

	public boolean hasParent() {

		return ordinal() != 0;
	}

	public EExecutionType getParent() {

		return EExecutionType.values()[ordinal() - 1];
	}

	/**
	 * Convenience method
	 * 
	 * @return
	 */
	public String literal() {

		return toString().toLowerCase();
	}
}
