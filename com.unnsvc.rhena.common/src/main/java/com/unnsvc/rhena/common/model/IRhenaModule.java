
package com.unnsvc.rhena.common.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.repository.RepositoryIdentifier;

public interface IRhenaModule extends Iterable<IRhenaEdge>, Serializable {

	public IRhenaEdge getParent();

	public void setParent(IRhenaEdge parent);

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

	public void addDeclaredConfiguration(ILifecycleConfiguration declaredConfiguration);

	public List<ILifecycleConfiguration> getDeclaredConfigurations();

	public EModuleType getModuleType();

	public void setModuleType(EModuleType moduleType);

	public void setFramework(boolean framework);

	public boolean isFramework();
}
