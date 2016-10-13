
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
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

public class RhenaModule implements IRhenaModule {

	private ModuleIdentifier moduleIdentifier;
	private URI location;
	private IRepository repository;
	private String lifecycleName;
	private IRhenaEdge parentModule;
	private List<IRhenaEdge> dependencyEdges;
	private Properties properties;
	private Map<String, ILifecycleDeclaration> lifecyclesDeclarations;

	public RhenaModule(ModuleIdentifier moduleIdentifier, URI location, IRepository repository) {

		this.moduleIdentifier = moduleIdentifier;
		this.location = location;
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
	public URI getLocation() {

		return location;
	}

	@Override
	public void setParentModule(IRhenaEdge parentModule) {

		this.parentModule = parentModule;
	}

	@Override
	public IRhenaEdge getParentModule() {

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
	public <T extends IModelVisitor> T visit(T visitor) throws RhenaException {

		visitor.visit(this);

		return visitor;
	}

	@Override
	public Map<String, ILifecycleDeclaration> getLifecycleDeclarations() {

		return lifecyclesDeclarations;
	}

	@Override
	public void setLifecycleDeclarations(Map<String, ILifecycleDeclaration> lifecycleDeclarations) {

		this.lifecyclesDeclarations = lifecycleDeclarations;
	}

	@Override
	public ILifecycleDeclaration getLifecycleDeclaration(String lifecycleName) throws RhenaException {

		ILifecycleDeclaration decl = lifecyclesDeclarations.get(lifecycleName);
		if (decl == null) {
			if (parentModule != null) {
				return parentModule.getTarget().getLifecycleDeclaration(lifecycleName);
			} else {
				throw new RhenaException("Lifecycle " + lifecycleName + " was not found");
			}
		}
		return decl;
	}

}
