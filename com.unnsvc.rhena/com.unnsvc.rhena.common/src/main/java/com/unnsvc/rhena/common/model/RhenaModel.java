
package com.unnsvc.rhena.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IVisitableModel;
import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class RhenaModel extends RhenaReference implements IVisitableModel {

	private IRepository repository;
	private RhenaEdge lifecycleModule;
	private RhenaEdge parentModule;
	private List<RhenaEdge> dependencyEdges;
	private Properties properties;

	public RhenaModel(ModuleIdentifier moduleIdentifier, IRepository repository) {

		super(moduleIdentifier);
		this.repository = repository;
		this.dependencyEdges = new ArrayList<RhenaEdge>();
		this.properties = new Properties();
	}

	public void setParentModule(RhenaEdge parentModule) {

		this.parentModule = parentModule;
	}

	public RhenaEdge getParentModule() {

		return parentModule;
	}

	public void setLifecycleModule(RhenaEdge lifecycleModule) {

		this.lifecycleModule = lifecycleModule;
	}

	public RhenaEdge getLifecycleModule() {

		return lifecycleModule;
	}

	public void addDependencyEdge(RhenaEdge dependencyEdge) {

		this.dependencyEdges.add(dependencyEdge);
	}

	public List<RhenaEdge> getDependencyEdges() {

		return dependencyEdges;
	}

	public void setProperty(String key, String value) {

		this.properties.setProperty(key, value);
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
