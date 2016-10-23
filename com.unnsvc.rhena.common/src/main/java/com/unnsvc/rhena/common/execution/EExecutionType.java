
package com.unnsvc.rhena.common.execution;

public enum EExecutionType implements Comparable<EExecutionType> {

	/**
	 * Keep it simple without any processing overhead if we were to chain one to
	 * one to one
	 *
	 */
	MODEL, 
	DELIVERABLE(MODEL), 
	FRAMEWORK(MODEL), 
	TEST(MODEL, DELIVERABLE, FRAMEWORK), 
	INTEGRATION(MODEL, DELIVERABLE, FRAMEWORK, TEST), 
	PROTOTYPE(MODEL, DELIVERABLE, FRAMEWORK, TEST);

	private EExecutionType[] traversables;

	EExecutionType(EExecutionType... traversables) {

		this.traversables = traversables;
	}

	EExecutionType() {

		this.traversables = new EExecutionType[] {};
	}

	public boolean canTraverse(EExecutionType that) {

		if(that == null) {
			return false;
		}
		
		if (this == that) {
			return true;
		}

		for (EExecutionType supertype : traversables) {
			if (that == supertype) {
				return true;
			}
		}

		return false;
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
