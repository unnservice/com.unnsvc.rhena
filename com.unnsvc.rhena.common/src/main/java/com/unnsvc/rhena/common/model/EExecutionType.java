
package com.unnsvc.rhena.common.model;

/**
 * Ordering is important here
 * 
 * @author noname
 *
 */
public enum EExecutionType {

	MAIN, TEST, PROTO, ITEST;

	public boolean lessOrEqualTo(EExecutionType other) {

		return this.ordinal() <= other.ordinal();
	}

	public boolean greaterOrEqualTo(EExecutionType other) {

		return this.ordinal() >= other.ordinal();
	}
}
