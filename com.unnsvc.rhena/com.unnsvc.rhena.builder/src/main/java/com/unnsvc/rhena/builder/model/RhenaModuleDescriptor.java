
package com.unnsvc.rhena.builder.model;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.resolvers.RhenaMaterialiser;
import com.unnsvc.rhena.builder.visitors.IVisitable;
import com.unnsvc.rhena.builder.visitors.IVisitor;

public class RhenaModuleDescriptor implements IVisitable {

	private RhenaMaterialiser repository;
	private ModuleIdentifier moduleIdentifier;
	private ModuleIdentifier parentModule;
	private ModuleIdentifier lifecycleModule;
	private List<RhenaModuleEdge> dependencyEdges;

	public RhenaModuleDescriptor(RhenaMaterialiser repository) {

		this.repository = repository;
		this.dependencyEdges = new ArrayList<RhenaModuleEdge>();
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	public void setParentModule(ModuleIdentifier parentModule) {

		this.parentModule = parentModule;
	}

	public ModuleIdentifier getParentModule() {

		return parentModule;
	}

	public void setModuleIdentifier(ModuleIdentifier moduleIdentifier) {

		this.moduleIdentifier = moduleIdentifier;
	}

	public void setLifecycleModule(ModuleIdentifier lifecycleModule) {

		this.lifecycleModule = lifecycleModule;
	}

	public ModuleIdentifier getLifecycleModule() {

		return lifecycleModule;
	}

	public void addDependencyEdge(RhenaModuleEdge edge) {

		if (!dependencyEdges.contains(edge)) {
			this.dependencyEdges.add(edge);
		}
	}

	public List<RhenaModuleEdge> getDependencyEdges() {

		return dependencyEdges;
	}

	@Override
	public void visit(IVisitor visitor) throws RhenaException {

		visitor.startVisit(this);

		visitor.endVisit(this);
	}

	public RhenaMaterialiser getRepository() {

		return repository;
	}
}
