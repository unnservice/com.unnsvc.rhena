
package com.unnsvc.rhena.common.model;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.common.visitors.IVisitableModel;

public interface IRhenaModule extends IVisitableModel, Serializable {

	public ModuleIdentifier getIdentifier();

	public URI getLocation();

	public IRepository getRepository();

	public void setParent(IRhenaEdge parent);

	public IRhenaEdge getParent();

	public void setProperty(String name, String value);

	public void setLifecycleName(String lifecycleName);

	public String getLifecycleName();

	public Map<String, ILifecycleReference> getLifecycleDeclarations();

	public void setLifecycleDeclarations(Map<String, ILifecycleReference> lifecycleDeclarations);

	public List<IRhenaEdge> getDependencies();

	public void setDependencies(List<IRhenaEdge> dependencies);

	public Properties getProperties();

	public void setProperties(Properties properties);

	public void setModuleType(ERhenaModuleType moduleType);

	public ERhenaModuleType getModuleType();

}
