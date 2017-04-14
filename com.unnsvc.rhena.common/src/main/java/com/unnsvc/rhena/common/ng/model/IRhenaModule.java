
package com.unnsvc.rhena.common.ng.model;

import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.repository.RepositoryIdentifier;

public interface IRhenaModule {

	public IRhenaEdge getParent();

	public void setParent(IRhenaEdge parent);

	public ERhenaModuleType getModuleType();

	public void setModuleType(ERhenaModuleType moduleType);

	public void setLifecycleConfiguration(ILifecycleConfiguration lifecycleConfiguration);

	public ILifecycleConfiguration getLifecycleConfiguration();

	public ModuleIdentifier getIdentifier();

	public void setIdentifier(ModuleIdentifier identifier);

	public RepositoryIdentifier getRepositoryIdentifier();

	public void setRepositoryIdentifier(RepositoryIdentifier repositoryIdentifier);

	public void addDependency(IRhenaEdge dependency);

	public List<IRhenaEdge> getDependencies();

	public void setProperty(String name, String value);

	public void setDependencies(List<IRhenaEdge> dependencies);

	public String getProperty(String name);

	public Map<String, String> getProperties();

	public void setProperties(Map<String, String> properties);

}
