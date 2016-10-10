package com.unnsvc.rhena.lifecycle;

import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModule;

public interface ILifecycleFactory {

	public IResourcesProcessor newResourceProcessor(RhenaModule model, RhenaExecutionType type);

}
