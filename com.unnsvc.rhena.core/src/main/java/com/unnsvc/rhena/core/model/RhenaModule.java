
package com.unnsvc.rhena.core.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.NotExistsException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.ModuleType;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

public class RhenaModule implements IRhenaModule {

	private ModuleIdentifier moduleIdentifier;
	private ModuleType moduleType;
	private List<IRhenaEdge> dependencyEdges;
	private String lifecycleName;
	private Map<String, ILifecycleDeclaration> lifecyclesDeclarations;
	private URI location;
	private IRhenaEdge parentModule;
	private Properties properties;
	private IRepository repository;

	public RhenaModule(ModuleType moduleType, ModuleIdentifier moduleIdentifier, URI location, IRepository repository) {

		this.moduleType = moduleType;
		this.moduleIdentifier = moduleIdentifier;
		this.location = location;
		this.repository = repository;
		this.dependencyEdges = new ArrayList<IRhenaEdge>();
		this.properties = new Properties();
		this.lifecyclesDeclarations = new HashMap<String, ILifecycleDeclaration>();
	}

	@Override
	public List<IRhenaEdge> getDependencyEdges() {

		return dependencyEdges;
	}

	@Override
	public ILifecycleDeclaration getLifecycleDeclaration(String lifecycleName) throws NotExistsException {

		ILifecycleDeclaration decl = lifecyclesDeclarations.get(lifecycleName);
		if (decl == null) {
			if (parentModule != null) {
				return parentModule.getTarget().getLifecycleDeclaration(lifecycleName);
			} else {
				throw new NotExistsException("Lifecycle " + lifecycleName + " was not found");
			}
		}
		return decl;
	}

	@Override
	public Map<String, ILifecycleDeclaration> getLifecycleDeclarations() {

		return lifecyclesDeclarations;
	}

	@Override
	public String getLifecycleName() {

		return lifecycleName;
	}

	@Override
	public URI getLocation() {

		return location;
	}

	@Override
	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	@Override
	public ModuleType getModuleType() {

		return moduleType;
	}

	@Override
	public IRhenaEdge getParentModule() {

		return parentModule;
	}

	@Override
	public Properties getProperties() {

		return properties;
	}

	@Override
	public IRepository getRepository() {

		return repository;
	}

	/**
	 * @TODO Remove this, make getLifecycleDeclaration(lifecycleName) return
	 *       null if not found, and throw exceptions where that method is used
	 */
	@Override
	public boolean hasLifecycleDeclaration(String lifecycleName) throws RhenaException {

		if (lifecyclesDeclarations.get(lifecycleName) != null) {
			return true;
		}

		if (parentModule != null) {
			return parentModule.getTarget().hasLifecycleDeclaration(lifecycleName);
		}

		return false;
	}

	@Override
	public void setDependencyEdges(List<IRhenaEdge> dependencyEdges) {

		this.dependencyEdges = dependencyEdges;
	}

	@Override
	public void setLifecycleDeclarations(Map<String, ILifecycleDeclaration> lifecycleDeclarations) {

		this.lifecyclesDeclarations = lifecycleDeclarations;
	}

	@Override
	public void setLifecycleName(String lifecycleName) {

		this.lifecycleName = lifecycleName;
	}

	@Override
	public void setParentModule(IRhenaEdge parentModule) {

		this.parentModule = parentModule;
	}

	@Override
	public void setProperties(Properties properties) {

		this.properties = properties;
	}

	@Override
	public void setProperty(String key, String value) {

		this.properties.setProperty(key, value);
	}

	@Override
	public <T extends IModelVisitor> T visit(T visitor) throws RhenaException {

		visitor.visit(this);

		return visitor;
	}

}
