
package com.unnsvc.rhena.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IVisitable;
import com.unnsvc.rhena.common.IVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class RhenaModel extends RhenaReference implements IVisitable {

	private IRepository repository;
	private RhenaReference lifecycleModule;
	private RhenaReference parentModule;
	private List<RhenaEdge> dependencyEdges;
	private Properties properties;

	public RhenaModel(ModuleIdentifier moduleIdentifier, IRepository repository) {

		super(moduleIdentifier);
		this.repository = repository;
		this.dependencyEdges = new ArrayList<RhenaEdge>();
		this.properties = new Properties();
	}

	public void setParentModule(RhenaReference parentModule) {

		this.parentModule = parentModule;
	}

	public RhenaReference getParentModule() {

		return parentModule;
	}

	public void setLifecycleModule(RhenaReference lifecycleModule) {

		this.lifecycleModule = lifecycleModule;
	}

	public RhenaReference getLifecycleModule() {

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
	public void visit(IVisitor visitor) throws RhenaException {

		visitor.startModule(this);

		visitor.endModule(this);
	}
}
