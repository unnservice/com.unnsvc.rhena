package com.unnsvc.rhena.lifecycle;

import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.RhenaModule;

public class DefaultLifecycleFactory implements ILifecycleFactory {

	@Override
	public IResourcesProcessor newResourceProcessor(RhenaModule model, ExecutionType type) {

		return new DefaultResourcesProcessor(model, type);
	}

}
