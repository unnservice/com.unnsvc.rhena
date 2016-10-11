package com.unnsvc.rhena.lifecycle;

import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface ILifecycleFactory {

	public IResourcesProcessor newResourceProcessor(IRhenaModule model, ExecutionType type);

}
