
package com.unnsvc.rhena.common.ng.model;

public interface IRhenaModule {

	public IRhenaEdge getParent();

	public void setParent(IRhenaEdge parent);

	public ERhenaModuleType getModuleType();

	public void setModuleType(ERhenaModuleType moduleType);

	public void setLifecycleConfiguration(ILifecycleConfiguration lifecycleConfiguration);

	public ILifecycleConfiguration getLifecycleConfiguration();

}
