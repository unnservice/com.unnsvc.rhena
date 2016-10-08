
package com.unnsvc.rhena.lifecycle;

import com.unnsvc.rhena.common.model.CompositeScope;
import com.unnsvc.rhena.common.model.RhenaModule;

public interface IResourcesLifecycle extends ILifecycle {

	public IResourcesLifecycle newDefaultResourcesLifecycle(RhenaModule model, CompositeScope scope);

	public void compileResources();

}
