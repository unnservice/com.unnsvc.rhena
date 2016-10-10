
package com.unnsvc.rhena.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IVisitableModel;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.lifecycle.LifecycleDeclaration;

public class RhenaModel extends RhenaReference implements IVisitableModel {

	private IRepository repository;
	private String lifecycleName;
	private ModuleIdentifier parentModule;
	private List<RhenaEdge> dependencyEdges;
	private Properties properties;
	private Map<String, LifecycleDeclaration> lifecycleDeclarations;

	public RhenaModel(ModuleIdentifier moduleIdentifier, IRepository repository) {

		super(moduleIdentifier);
		this.repository = repository;
		this.dependencyEdges = new ArrayList<RhenaEdge>();
		this.properties = new Properties();
		this.lifecycleDeclarations = new HashMap<String, LifecycleDeclaration>();
	}

	public void setParentModule(ModuleIdentifier parentModule) {

		this.parentModule = parentModule;
	}

	public ModuleIdentifier getParentModule() {

		return parentModule;
	}

	public void setLifecycleName(String lifecycleName) {

		this.lifecycleName = lifecycleName;
	}

	public String getLifecycleName() {

		return lifecycleName;
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

	public void addLifecycleDeclaration(LifecycleDeclaration lifecycleDeclaration) {

		this.lifecycleDeclarations.put(lifecycleDeclaration.getName(), lifecycleDeclaration);
	}

	public Map<String, LifecycleDeclaration> getLifecycleDeclarations() {

		return lifecycleDeclarations;
	}
}
