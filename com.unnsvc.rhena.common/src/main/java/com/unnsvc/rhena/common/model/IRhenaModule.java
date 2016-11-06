
package com.unnsvc.rhena.common.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.visitors.IVisitableModel;

public interface IRhenaModule extends IVisitableModel {

	public ModuleIdentifier getIdentifier();

	public IRepository getRepository();

	public void setParent(IRhenaEdge parent);

	public IRhenaEdge getParent();
	
	public void setProperty(String name, String value);

	public void setLifecycleName(String lifecycleName);

	public Map<String, ILifecycleDeclaration> getLifecycleDeclarations();

	public List<IRhenaEdge> getDependencies();

	public String getLifecycleName();

	/**
	 * Convenience method extracing all of the relationships from the module
	 * @return
	 */
	public List<IRhenaEdge> getRelationships();

	// public ModuleIdentifier getModuleIdentifier();
	//
	// public URI getLocation();
	//
	// public ModuleType getModuleType();
	//
	// public IRhenaEdge getParentModule();
	//
	// public void setParentModule(IRhenaEdge parentModule);
	//
	// public String getLifecycleName();
	//
	// public List<IRhenaEdge> getDependencyEdges();
	//
	// public Properties getProperties();
	//
	// public IRepository getRepository();
	//
	// public Map<String, ILifecycleDeclaration> getLifecycleDeclarations();
	//
	// public void setLifecycleDeclarations(Map<String, ILifecycleDeclaration>
	// lifecycleDeclarations);
	//
	// public void setLifecycleName(String lifecycleName);
	//
	// public void setDependencyEdges(List<IRhenaEdge> dependencyEdges);
	//
	// public void setProperty(String key, String value);
	//
	// public void setProperties(Properties properties);
	//
	// /**
	// * Traverse parents until we find lifecycle
	// *
	// * @param lifecycleName
	// * @return
	// * @throws RhenaException
	// * if no lifecycle with that name was found
	// */
	// public ILifecycleDeclaration getLifecycleDeclaration(String
	// lifecycleName) throws RhenaException;
	//
	// public boolean hasLifecycleDeclaration(String lifecycleName) throws
	// RhenaException;

}
