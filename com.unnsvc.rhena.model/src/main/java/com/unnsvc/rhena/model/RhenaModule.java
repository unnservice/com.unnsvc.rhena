
package com.unnsvc.rhena.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EModuleType;
import com.unnsvc.rhena.common.model.ILifecycleSpecification;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.RepositoryIdentifier;

public class RhenaModule implements IRhenaModule {

	private static final long serialVersionUID = 1L;
	private ModuleIdentifier identifier;
	private EModuleType moduleType;
	private boolean framework;
	private RepositoryIdentifier repositoryIdentifier;
	private IRhenaEdge parent;
	private ILifecycleSpecification lifecycleSpecification;
	private List<IRhenaEdge> dependencies;
	private Map<String, String> properties;
	private List<ILifecycleSpecification> declaredLifecycleSpecifications;

	public RhenaModule(ModuleIdentifier identifier, RepositoryIdentifier repositoryIdentifier) {

		this.identifier = identifier;
		this.framework = false;
		this.repositoryIdentifier = repositoryIdentifier;
		this.dependencies = new ArrayList<IRhenaEdge>();
		this.properties = new HashMap<String, String>();
		this.declaredLifecycleSpecifications = new ArrayList<ILifecycleSpecification>();
		this.moduleType = EModuleType.REMOTE;
	}

	@Override
	public ModuleIdentifier getIdentifier() {

		return identifier;
	}

	@Override
	public void setIdentifier(ModuleIdentifier identifier) {

		this.identifier = identifier;
	}

	@Override
	public RepositoryIdentifier getRepositoryIdentifier() {

		return repositoryIdentifier;
	}

	@Override
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
	public void setLifecycleSpecification(ILifecycleSpecification lifecycleConfiguration) {

		this.lifecycleSpecification = lifecycleConfiguration;
	}

	@Override
	public ILifecycleSpecification getLifecycleSpecification() {

		return lifecycleSpecification;
	}

	@Override
	public void addDependency(IRhenaEdge dependency) {

		this.dependencies.add(dependency);
	}

	@Override
	public List<IRhenaEdge> getDependencies() {

		return dependencies;
	}

	@Override
	public void setDependencies(List<IRhenaEdge> dependencies) {

		this.dependencies = dependencies;
	}

	@Override
	public void setProperty(String name, String value) {

		this.properties.put(name, value);
	}

	@Override
	public String getProperty(String name) {

		return properties.get(name);
	}

	@Override
	public void setProperties(Map<String, String> properties) {

		this.properties = properties;
	}

	@Override
	public Map<String, String> getProperties() {

		return properties;
	}

	@Override
	public Iterator<IRhenaEdge> iterator() {

		return dependencies.iterator();
	}

	@Override
	public void addDeclaredLifecycleSpecification(ILifecycleSpecification lifecycleSpecification) {

		this.declaredLifecycleSpecifications.add(lifecycleSpecification);
	}

	@Override
	public List<ILifecycleSpecification> getDeclaredLifecycleSpecifications() {

		return declaredLifecycleSpecifications;
	}

	@Override
	public EModuleType getModuleType() {

		return moduleType;
	}

	@Override
	public void setModuleType(EModuleType moduleType) {

		this.moduleType = moduleType;
	}

	@Override
	public void setFramework(boolean framework) {

		this.framework = framework;
	}

	@Override
	public boolean isFramework() {

		return framework;
	}
}
