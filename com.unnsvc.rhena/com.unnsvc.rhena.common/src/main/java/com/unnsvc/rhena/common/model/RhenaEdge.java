
package com.unnsvc.rhena.common.model;

public class RhenaEdge {

	private RhenaEdgeType dependencyType;
	private RhenaReference target;

	public RhenaEdge(RhenaEdgeType dependencyType, RhenaReference target) {

		this.dependencyType = dependencyType;
		this.target = target;
	}

	public RhenaEdgeType getDependencyType() {

		return dependencyType;
	}

	public RhenaReference getTarget() {

		return target;
	}

	public void setTarget(RhenaReference target) {

		this.target = target;
	}
}
