
package com.unnsvc.rhena.common.model;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.visitors.IVisitableModel;

public interface IRhenaModule extends IVisitableModel {

	public ModuleIdentifier getModuleIdentifier();

	public IRhenaEdge getParentModule();

	public void setParentModule(IRhenaEdge parentModule);

	public String getLifecycleName();

	public List<IRhenaEdge> getDependencyEdges();

	public Properties getProperties();

	public IRepository getRepository();

	public Map<String, ILifecycleDeclaration> getLifecycleDeclarations();

	public void setLifecycleDeclarations(Map<String, ILifecycleDeclaration> lifecycleDeclarations);

	public void setLifecycleName(String lifecycleName);

	public void setDependencyEdges(List<IRhenaEdge> dependencyEdges);

	public void setProperty(String key, String value);

	public void setProperties(Properties properties);

	/**
	 * Traverse parents until we find lifecycle
	 * @param lifecycleName
	 * @return
	 * @throws RhenaException if no lifecycle with that name was found
	 */
	public ILifecycleDeclaration getLifecycleDeclaration(String lifecycleName) throws RhenaException;
}
