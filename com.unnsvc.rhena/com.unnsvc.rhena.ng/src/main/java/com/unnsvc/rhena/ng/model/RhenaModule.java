
package com.unnsvc.rhena.ng.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModuleEdge;
import com.unnsvc.rhena.ng.model.visitors.IVisitable;
import com.unnsvc.rhena.ng.model.visitors.IVisitor;

public class RhenaModule implements IVisitable {

	private ModuleIdentifier moduleIdentifier;
	private ModuleIdentifier lifecycleDeclaration;
	private ModuleIdentifier parentModule;
	private List<RhenaModuleEdge> dependencyEdges;
	private Properties properties;

	public RhenaModule(ModuleIdentifier moduleIdentifier) {

		this.moduleIdentifier = moduleIdentifier;
		this.dependencyEdges = new ArrayList<RhenaModuleEdge>();
		this.properties = new Properties();
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	@Override
	public void visit(IVisitor visitor) throws RhenaException {

		visitor.startModule(this);

		visitor.endModule(this);
	}

	public String getSourceDirectoryName() {

		return moduleIdentifier.getComponentName().toString() + "." + moduleIdentifier.getModuleName().toString();
	}

	public void setParentModule(ModuleIdentifier parentModule) {

		this.parentModule = parentModule;
	}

	public ModuleIdentifier getParentModule() {

		return parentModule;
	}

	public void setLifecycleModule(ModuleIdentifier lifecycleDeclaration) {

		this.lifecycleDeclaration = lifecycleDeclaration;
	}

	public ModuleIdentifier getLifecycleDeclaration() {

		return lifecycleDeclaration;
	}

	public void addDependencyEdge(RhenaModuleEdge dependencyEdge) {

		this.dependencyEdges.add(dependencyEdge);
	}

	public List<RhenaModuleEdge> getDependencyEdges() {

		return dependencyEdges;
	}

	public void setProperty(String key, String value) {

		this.properties.setProperty(key, value);
	}

	public Properties getProperties() {

		return properties;
	}
}
