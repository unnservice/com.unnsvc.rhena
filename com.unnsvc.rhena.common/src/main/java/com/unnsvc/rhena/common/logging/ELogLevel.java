
package com.unnsvc.rhena.common.logging;

public enum ELogLevel {

	TRACE, DEBUG, INFO, WARN, ERROR;
	
	public boolean isGreaterOrEquals(ELogLevel other) {
		
		return this.compareTo(other) >= 0;
	}
}
