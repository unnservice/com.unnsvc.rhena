
package com.unnsvc.rhena.common.model;

public class RhenaModuleEdge {

	private CompositeScope scope;
	private ModuleIdentifier target;

	public RhenaModuleEdge(CompositeScope scope, ModuleIdentifier target) {

		this.scope = scope;
		this.target = target;
	}

	public CompositeScope getScope() {

		return scope;
	}

	public ModuleIdentifier getTarget() {

		return target;
	}
}
