
package com.unnsvc.rhena.builder.model;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;

public class RhenaModule {

	private ModuleIdentifier moduleIdentifier;
	private ModuleIdentifier parentModule;
	private ModuleIdentifier lifecycleDeclaration;
	private List<RhenaModuleEdge> dependencyEdges;

	public RhenaModule() {

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

	public void setLifecycleDeclaration(ModuleIdentifier lifecycleDeclaration) {

		this.lifecycleDeclaration = lifecycleDeclaration;
	}

	public void addDependencyEdge(RhenaModuleEdge edge) {

		if (!dependencyEdges.contains(edge)) {
			this.dependencyEdges.add(edge);
		}
	}

}
