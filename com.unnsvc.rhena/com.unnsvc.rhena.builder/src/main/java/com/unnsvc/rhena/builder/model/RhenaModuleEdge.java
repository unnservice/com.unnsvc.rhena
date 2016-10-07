
package com.unnsvc.rhena.builder.model;

import com.unnsvc.rhena.builder.CompositeScope;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.visitors.IVisitor;

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
