
package com.unnsvc.rhena.core.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.common.model.ERhenaModuleType;
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
	private ERhenaModuleType moduleType;

	public RhenaModule(ModuleIdentifier identifier, URI location, IRepository repository) {

		this.identifier = identifier;
		this.location = location;
		this.repository = repository;
		this.properties = new Properties();
		this.lifecycleDeclarations = new HashMap<String, ILifecycleReference>();
		this.dependencies = new ArrayList<IRhenaEdge>();
		this.lifecycleName = RhenaConstants.DEFAULT_LIFECYCLE_NAME;
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
	public Map<String, ILifecycleReference> getDeclaredLifecycleDeclarations() {

		return lifecycleDeclarations;
	}
	
	@Override
	public Map<String, ILifecycleReference> getMergedLifecycleDeclarations(IRhenaCache cache) {

		if(getParent() != null) {
			IRhenaModule parentModule = cache.getModule(getParent().getEntryPoint().getTarget());
			Map<String, ILifecycleReference> mergedLifecycleReferences = new HashMap<String, ILifecycleReference>(parentModule.getDeclaredLifecycleDeclarations());
			mergedLifecycleReferences.putAll(getDeclaredLifecycleDeclarations());
			return mergedLifecycleReferences;
		}
		
		return getDeclaredLifecycleDeclarations();
	}

	@Override
	public void setLifecycleDeclarations(Map<String, ILifecycleReference> lifecycleDeclarations) {

		this.lifecycleDeclarations = lifecycleDeclarations;
	}

	@Override
	public List<IRhenaEdge> getDeclaredDependencies() {

		return dependencies;
	}

	@Override
	public List<IRhenaEdge> getMergedDependencies(IRhenaCache cache) {

		if (getParent() != null) {

			IRhenaModule parentModule = cache.getModule(getParent().getEntryPoint().getTarget());
			List<IRhenaEdge> mergedEdges = new ArrayList<IRhenaEdge>(parentModule.getMergedDependencies(cache));
			for (IRhenaEdge declared : getDeclaredDependencies()) {
				if (!mergedEdges.contains(declared)) {
					mergedEdges.add(declared);
				}
			}
		}
		return getDeclaredDependencies();
	}

	@Override
	public void setDependencies(List<IRhenaEdge> dependencies) {

		this.dependencies = dependencies;
	}

	@Override
	public Properties getDeclaredProperties() {

		return properties;
	}

	@Override
	public Properties getMergedProperties(IRhenaCache cache) {

		if (getParent() != null) {
			IRhenaModule parentModule = cache.getModule(getParent().getEntryPoint().getTarget());
			Properties mergedProperties = new Properties(parentModule.getMergedProperties(cache));
			mergedProperties.putAll(getDeclaredProperties());
			return mergedProperties;
		}
		return getDeclaredProperties();
	}

	@Override
	public void setProperties(Properties properties) {

		this.properties = properties;
	}

	@Override
	public void setModuleType(ERhenaModuleType moduleType) {

		this.moduleType = moduleType;
	}

	@Override
	public ERhenaModuleType getModuleType() {

		return moduleType;
	}
}
