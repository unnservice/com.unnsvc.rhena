
package com.unnsvc.rhena.core.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

public class RhenaModule implements IRhenaModule {

	private static final long serialVersionUID = 1L;
	private ModuleIdentifier identifier;
	private URI location;
	private transient IRepository repository;
	private IRhenaEdge parent;
	private Properties properties;
	private String lifecycleName;
	private Map<String, ILifecycleReference> lifecycleDeclarations;
	private List<IRhenaEdge> dependencies;

	public RhenaModule(ModuleIdentifier identifier, URI location, IRepository repository) {

		this.identifier = identifier;
		this.location = location;
		this.repository = repository;
		this.properties = new Properties();
		this.lifecycleDeclarations = new HashMap<String, ILifecycleReference>();
		this.dependencies = new ArrayList<IRhenaEdge>();
	}

	@Override
	public ModuleIdentifier getIdentifier() {

		return identifier;
	}

	@Override
	public URI getLocation() {

		return location;
	}

	@Override
	public IRepository getRepository() {

		return repository;
	}

	@Override
	public void setParent(IRhenaEdge parent) {

		this.parent = parent;
	}

	@Override
	public IRhenaEdge getParent() {

		return parent;
	}

	@Override
	public <T extends IModelVisitor> T visit(T visitor) throws RhenaException {

		visitor.visit(this);
		return visitor;
	}

	@Override
	public void setProperty(String name, String value) {

		this.properties.put(name, value);
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
	public Map<String, ILifecycleReference> getLifecycleDeclarations() {

		return lifecycleDeclarations;
	}

	@Override
	public void setLifecycleDeclarations(Map<String, ILifecycleReference> lifecycleDeclarations) {

		this.lifecycleDeclarations = lifecycleDeclarations;
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
	public Properties getProperties() {

		return properties;
	}

	@Override
	public void setProperties(Properties properties) {

		this.properties = properties;
	}
}
