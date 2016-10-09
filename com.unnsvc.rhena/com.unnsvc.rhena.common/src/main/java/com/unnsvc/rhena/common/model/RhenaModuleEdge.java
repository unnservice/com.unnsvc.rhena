
package com.unnsvc.rhena.common.model;

public class RhenaModuleEdge {

	private DependencyType dependencyType;
	private ModuleIdentifier target;

	public RhenaModuleEdge(DependencyType dependencyType, ModuleIdentifier target) {

		this.dependencyType = dependencyType;
		this.target = target;
	}

	public DependencyType getDependencyType() {

		return dependencyType;
	}

	public ModuleIdentifier getTarget() {

		return target;
	}
}
