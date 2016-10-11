
package com.unnsvc.rhena.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;

public class RhenaModule implements IRhenaModule {

	private ModuleIdentifier moduleIdentifier;
	private IRepository repository;
	private String lifecycleName;
	private IRhenaModule parentModule;
	private List<IRhenaEdge> dependencyEdges;
	private Properties properties;
	private Map<String, ILifecycleDeclaration> lifecyclesDeclarations;

	public RhenaModule(ModuleIdentifier moduleIdentifier, IRepository repository) {

		this.moduleIdentifier = moduleIdentifier;
		this.repository = repository;
		this.dependencyEdges = new ArrayList<IRhenaEdge>();
		this.properties = new Properties();
		this.lifecyclesDeclarations = new HashMap<String, ILifecycleDeclaration>();
	}

	@Override
	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	@Override
	public void setParentModule(IRhenaModule parentModule) {

		this.parentModule = parentModule;
	}

	@Override
	public IRhenaModule getParentModule() {

		return parentModule;
	}

	@Override
	public void setLifecycleName(String lifecycleName) {

		this.lifecycleName = lifecycleName;
	}

	@Override
	public String getLifecycleName() {

		return lifecycleName;
	}

	@Override
	public void setDependencyEdges(List<IRhenaEdge> dependencyEdges) {

		this.dependencyEdges = dependencyEdges;
	}

	@Override
	public List<IRhenaEdge> getDependencyEdges() {

		return dependencyEdges;
	}

	@Override
	public void setProperty(String key, String value) {

		this.properties.setProperty(key, value);
	}

	@Override
	public void setProperties(Properties properties) {

		this.properties = properties;
	}

	@Override
	public Properties getProperties() {

		return properties;
	}

	@Override
	public IRepository getRepository() {

		return repository;
	}

	@Override
	public void visit(IModelVisitor visitor) throws RhenaException {

		visitor.startModel(this);

		visitor.endModel(this);
	}

	@Override
	public Map<String, ILifecycleDeclaration> getLifecycleDeclarations() {

		return lifecyclesDeclarations;
	}

	@Override
	public void setLifecycleDeclarations(Map<String, ILifecycleDeclaration> lifecycleDeclarations) {

		this.lifecyclesDeclarations = lifecycleDeclarations;
	}
}
