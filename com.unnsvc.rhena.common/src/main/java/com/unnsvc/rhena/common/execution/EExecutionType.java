
package com.unnsvc.rhena.common.execution;

public enum EExecutionType implements Comparable<EExecutionType> {

	/**
	 * Keep it simple in a flat model without hierarchies
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

		if (that == null) {
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

	public EExecutionType[] getTraversables() {

		return traversables;
	}

	/**
	 * Convenience method
	 * 
	 * @return
	 */
	public String literal() {

		return toString().toLowerCase();
	}

	/**
	 * If this is a parent execution type of that. This method is different from
	 * canTraverse in that it performs a forward check
	 * 
	 * @param executionType
	 * @return
	 */
	public boolean isParentOf(EExecutionType that) {

		for (EExecutionType traversable : traversables) {
			if (traversable.equals(that)) {
				return true;
			}
		}
		return false;
	}
}
