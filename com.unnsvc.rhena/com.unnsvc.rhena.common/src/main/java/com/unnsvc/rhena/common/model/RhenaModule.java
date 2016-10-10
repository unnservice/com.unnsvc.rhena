
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

public class RhenaModule implements IVisitableModel {

	private ModuleIdentifier moduleIdentifier;
	private IRepository repository;
	private String lifecycleName;
	private RhenaModule parentModule;
	private List<RhenaEdge> dependencyEdges;
	private Properties properties;
	private Map<String, LifecycleDeclaration> lifecyclesDeclarations;

	public RhenaModule(ModuleIdentifier moduleIdentifier, IRepository repository) {

		this.moduleIdentifier = moduleIdentifier;
		this.repository = repository;
		this.dependencyEdges = new ArrayList<RhenaEdge>();
		this.properties = new Properties();
		this.lifecyclesDeclarations = new HashMap<String, LifecycleDeclaration>();
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	public void setParentModule(RhenaModule parentModule) {

		this.parentModule = parentModule;
	}

	public RhenaModule getParentModule() {

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

	public Map<String, LifecycleDeclaration> getLifecycleDeclarations() {

		return lifecyclesDeclarations;
	}

	public void setLifecycleDeclarations(Map<String, LifecycleDeclaration> lifecycleDeclarations) {

		this.lifecyclesDeclarations = lifecycleDeclarations;
	}
}
