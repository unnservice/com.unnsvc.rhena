
package com.unnsvc.rhena.core.model;

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
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

public class RhenaModule implements IRhenaModule {

	private ModuleIdentifier identifier;
	private IRepository repository;
	private IRhenaEdge parent;
	private Properties properties;
	private String lifecycleName;
	private Map<String, ILifecycleDeclaration> lifecycleDeclarations;
	private List<IRhenaEdge> dependencies;

	public RhenaModule(ModuleIdentifier identifier, IRepository repository) {

		this.identifier = identifier;
		this.repository = repository;
		this.properties = new Properties();
		this.lifecycleDeclarations = new HashMap<String, ILifecycleDeclaration>();
		this.dependencies = new ArrayList<IRhenaEdge>();
	}

	@Override
	public ModuleIdentifier getIdentifier() {

		return identifier;
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
	public Map<String, ILifecycleDeclaration> getLifecycleDeclarations() {

		return lifecycleDeclarations;
	}

	@Override
	public List<IRhenaEdge> getDependencies() {

		return dependencies;
	}

	@Override
	public List<IRhenaEdge> getRelationships() {

		List<IRhenaEdge> relationships = new ArrayList<IRhenaEdge>();
		relationships.add(getParent());
		for (ILifecycleDeclaration lifecycle : getLifecycleDeclarations().values()) {
			relationships.add(lifecycle.getContext().getModuleEdge());
			lifecycle.getProcessors().forEach(proc -> relationships.add(proc.getModuleEdge()));
			relationships.add(lifecycle.getGenerator().getModuleEdge());
		}
		relationships.addAll(getDependencies());
		return relationships;
	}
}
