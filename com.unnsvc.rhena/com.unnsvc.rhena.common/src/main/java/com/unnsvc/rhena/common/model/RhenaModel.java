
package com.unnsvc.rhena.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IVisitableModel;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class RhenaModel extends RhenaReference implements IVisitableModel {

	private IRepository repository;
	private ModuleIdentifier lifecycleModule;
	private ModuleIdentifier parentModule;
	private List<RhenaEdge> dependencyEdges;
	private Properties properties;

	public RhenaModel(ModuleIdentifier moduleIdentifier, IRepository repository) {

		super(moduleIdentifier);
		this.repository = repository;
		this.dependencyEdges = new ArrayList<RhenaEdge>();
		this.properties = new Properties();
	}

	public void setParentModule(ModuleIdentifier parentModule) {

		this.parentModule = parentModule;
	}

	public ModuleIdentifier getParentModule() {

		return parentModule;
	}

	public void setLifecycleModule(ModuleIdentifier lifecycleModule) {

		this.lifecycleModule = lifecycleModule;
	}

	public ModuleIdentifier getLifecycleModule() {

		return lifecycleModule;
	}

	public void setDependencyEdges(List<RhenaEdge> dependencyEdges) {

		this.dependencyEdges = dependencyEdges;
	}

	public List<RhenaEdge> getDependencyEdges() {

		return dependencyEdges;
	}

	public void setProperty(String key, String value) {

		this.properties.setProperty(key, value);
	}

	public void setProperties(Properties properties) {

		this.properties = properties;
	}

	public Properties getProperties() {

		return properties;
	}

	public IRepository getRepository() {

		return repository;
	}

	@Override
	public void visit(IModelVisitor visitor) throws RhenaException {

		visitor.startModel(this);

		visitor.endModel(this);
	}
}
