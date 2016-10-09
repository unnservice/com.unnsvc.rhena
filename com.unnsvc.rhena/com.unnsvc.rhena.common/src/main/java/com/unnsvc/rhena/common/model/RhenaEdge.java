
package com.unnsvc.rhena.common.model;

public class RhenaEdge {

	private RhenaEdgeType dependencyType;
	private ModuleIdentifier target;

	public RhenaEdge(RhenaEdgeType dependencyType, ModuleIdentifier target) {

		this.dependencyType = dependencyType;
		this.target = target;
	}

	public RhenaEdgeType getDependencyType() {

		return dependencyType;
	}

	public ModuleIdentifier getTarget() {

		return target;
	}
}
