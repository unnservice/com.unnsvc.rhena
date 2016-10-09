
package com.unnsvc.rhena.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IVisitable;
import com.unnsvc.rhena.common.IVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class RhenaModule implements IVisitable {

	private ModuleState moduleState;
	private IRepository repository;
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

	public void setModuleState(ModuleState moduleState) {

		this.moduleState = moduleState;
	}

	public ModuleState getModuleState() {

		return moduleState;
	}

	public void setRepository(IRepository repository) {

		this.repository = repository;
	}

	public IRepository getRepository() {

		return repository;
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
