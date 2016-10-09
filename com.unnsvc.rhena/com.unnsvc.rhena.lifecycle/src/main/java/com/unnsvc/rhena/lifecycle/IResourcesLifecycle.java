
package com.unnsvc.rhena.lifecycle;

import com.unnsvc.rhena.common.model.DependencyType;
import com.unnsvc.rhena.common.model.RhenaModule;

public interface IResourcesLifecycle extends ILifecycle {

	public IResourcesLifecycle newDefaultResourcesLifecycle(RhenaModule model, DependencyType dependencyType);

	public void compileResources();

}
