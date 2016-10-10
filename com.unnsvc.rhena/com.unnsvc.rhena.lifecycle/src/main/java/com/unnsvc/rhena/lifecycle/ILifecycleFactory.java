package com.unnsvc.rhena.lifecycle;

import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModel;

public interface ILifecycleFactory {

	public IResourcesProcessor newResourceProcessor(RhenaModel model, RhenaExecutionType type);

}
