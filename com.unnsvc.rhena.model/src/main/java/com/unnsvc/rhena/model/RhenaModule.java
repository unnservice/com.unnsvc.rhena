
package com.unnsvc.rhena.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.ERhenaModuleType;
import com.unnsvc.rhena.common.ng.model.ILifecycleConfiguration;
import com.unnsvc.rhena.common.ng.model.IRhenaEdge;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.ng.repository.RepositoryIdentifier;

public class RhenaModule implements IRhenaModule {

	private ModuleIdentifier identifier;
	private ERhenaModuleType moduleType;
	private RepositoryIdentifier repositoryIdentifier;
	private IRhenaEdge parent;
	private ILifecycleConfiguration lifecycleConfiguration;
	private List<IRhenaEdge> dependencies;
	private Map<String, String> properties;

	public RhenaModule(ModuleIdentifier identifier, RepositoryIdentifier repositoryIdentifier) {

		this.identifier = identifier;
		this.repositoryIdentifier = repositoryIdentifier;
		this.dependencies = new ArrayList<IRhenaEdge>();
		this.properties = new HashMap<String, String>();
	}

	public ModuleIdentifier getIdentifier() {

		return identifier;
	}

	public void setIdentifier(ModuleIdentifier identifier) {

		this.identifier = identifier;
	}

	@Override
	public ERhenaModuleType getModuleType() {

		return moduleType;
	}

	@Override
	public void setModuleType(ERhenaModuleType moduleType) {

		this.moduleType = moduleType;
	}

	public RepositoryIdentifier getRepositoryIdentifier() {

		return repositoryIdentifier;
	}

	public void setRepositoryIdentifier(RepositoryIdentifier repositoryIdentifier) {

		this.repositoryIdentifier = repositoryIdentifier;
	}

	@Override
	public IRhenaEdge getParent() {

		return parent;
	}

	@Override
	public void setParent(IRhenaEdge parent) {

		this.parent = parent;
	}

	@Override
	public void setLifecycleConfiguration(ILifecycleConfiguration lifecycleConfiguration) {

		this.lifecycleConfiguration = lifecycleConfiguration;
	}

	@Override
	public ILifecycleConfiguration getLifecycleConfiguration() {

		return lifecycleConfiguration;
	}

	public void addDependency(IRhenaEdge dependency) {

		this.dependencies.add(dependency);
	}

	public List<IRhenaEdge> getDependencies() {

		return dependencies;
	}

	public void setDependencies(List<IRhenaEdge> dependencies) {

		this.dependencies = dependencies;
	}

	public void setProperty(String name, String value) {

		this.properties.put(name, value);
	}

	public String getProperty(String name) {

		return properties.get(name);
	}

	public Map<String, String> getProperties() {

		return properties;
	}

}
