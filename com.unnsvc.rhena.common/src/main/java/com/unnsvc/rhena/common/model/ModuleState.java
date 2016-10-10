
package com.unnsvc.rhena.common.model;

/**
 * All modules, whether local or remote, will be in one of these states.
 * 
 * @author noname
 *
 */
public enum ModuleState {

	UNRESOLVED, MODEL, RESOLVED;

	public String toLabel() {

		return toString().toLowerCase();
	}
}
