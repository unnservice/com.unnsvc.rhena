
package com.unnsvc.rhena.lifecycle;

import com.unnsvc.rhena.common.model.RhenaEdgeType;
import com.unnsvc.rhena.common.model.RhenaModel;

public interface IResourcesLifecycle extends ILifecycle {

	public IResourcesLifecycle newDefaultResourcesLifecycle(RhenaModel model, RhenaEdgeType dependencyType);

	public void compileResources();

}
