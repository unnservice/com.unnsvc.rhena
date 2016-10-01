
package com.unnsvc.rhena.builder.model;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.visitors.IVisitable;
import com.unnsvc.rhena.builder.visitors.IVisitor;

public class RhenaModule implements IVisitable {

	private ModuleIdentifier moduleIdentifier;
	private RhenaModuleEdge parentModule;
	private RhenaModuleEdge lifecycleModule;
	private List<RhenaModuleEdge> dependencyEdges;

	public RhenaModule() {

		this.dependencyEdges = new ArrayList<RhenaModuleEdge>();
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	public void setParentModule(RhenaModuleEdge parentModule) {

		this.parentModule = parentModule;
	}

	public RhenaModuleEdge getParentModule() {

		return parentModule;
	}

	public void setModuleIdentifier(ModuleIdentifier moduleIdentifier) {

		this.moduleIdentifier = moduleIdentifier;
	}

	public void setLifecycleModule(RhenaModuleEdge lifecycleModule) {

		this.lifecycleModule = lifecycleModule;
	}

	public RhenaModuleEdge getLifecycleModule() {

		return lifecycleModule;
	}

	public void addDependencyEdge(RhenaModuleEdge edge) {

		if (!dependencyEdges.contains(edge)) {
			this.dependencyEdges.add(edge);
		}
	}
	
	@Override
	public void visit(IVisitor visitor) {
		
		visitor.visiting(this);
	}

}
