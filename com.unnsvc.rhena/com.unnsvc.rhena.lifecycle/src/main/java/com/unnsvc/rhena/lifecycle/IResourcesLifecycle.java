
package com.unnsvc.rhena.lifecycle;

import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModel;

public interface IResourcesLifecycle extends ILifecycle {

	public IResourcesLifecycle newDefaultResourcesLifecycle(RhenaModel model, RhenaExecutionType dependencyType);

	public void compileResources();

}
