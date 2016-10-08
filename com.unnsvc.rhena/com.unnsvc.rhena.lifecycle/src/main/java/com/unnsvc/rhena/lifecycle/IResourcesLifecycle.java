package com.unnsvc.rhena.lifecycle;

import com.unnsvc.rhena.common.model.RhenaModule;

public interface IResourcesLifecycle extends ILifecycle {

	public void compileResources(RhenaModule model);

}
