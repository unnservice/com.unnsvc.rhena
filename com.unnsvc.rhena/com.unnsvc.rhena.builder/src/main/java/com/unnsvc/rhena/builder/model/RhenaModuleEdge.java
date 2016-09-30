
package com.unnsvc.rhena.builder.model;

import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;

public class RhenaModuleEdge {

	private Scope scope;
	private ModuleIdentifier target;

	public RhenaModuleEdge(Scope scope, ModuleIdentifier target) {

		this.scope = scope;
		this.target = target;
	}

}
