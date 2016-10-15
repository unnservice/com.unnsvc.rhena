
package com.unnsvc.rhena.core.model;

import java.net.URI;
import java.util.Collection;
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

public class RhenaReference implements IRhenaModule {

	private ModuleIdentifier moduleIdentifier;

	public RhenaReference(ModuleIdentifier moduleIdentifier) {

		this.moduleIdentifier = moduleIdentifier;
	}

	@Override
	public <T extends IModelVisitor> T visit(T visitor) throws RhenaException {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());
	}

	@Override
	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	@Override
	public URI getLocation() {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());
	}

	@Override
	public IRhenaEdge getParentModule() {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());
	}

	@Override
	public String getLifecycleName() {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());
	}

	@Override
	public List<IRhenaEdge> getDependencyEdges() {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());
	}

	@Override
	public Properties getProperties() {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());
	}

	@Override
	public IRepository getRepository() {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());
	}

	@Override
	public Map<String, ILifecycleDeclaration> getLifecycleDeclarations() {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());
	}

	@Override
	public void setLifecycleDeclarations(Map<String, ILifecycleDeclaration> lifecycleDeclarations) {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());

	}

	@Override
	public void setParentModule(IRhenaEdge parentModule) {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());

	}

	@Override
	public void setLifecycleName(String lifecycleName) {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());

	}

	@Override
	public void setDependencyEdges(List<IRhenaEdge> dependencyEdges) {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());

	}

	@Override
	public void setProperty(String key, String value) {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());

	}

	@Override
	public void setProperties(Properties properties) {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());

	}

	@Override
	public ILifecycleDeclaration getLifecycleDeclaration(String lifecycleName) throws RhenaException {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());
	}

	@Override
	public boolean hasLifecycleDeclaration(String lifecycleName) throws RhenaException {

		throw new UnsupportedOperationException("Call on model of yet undersolved reference for: " + moduleIdentifier.toString());
	}
}
