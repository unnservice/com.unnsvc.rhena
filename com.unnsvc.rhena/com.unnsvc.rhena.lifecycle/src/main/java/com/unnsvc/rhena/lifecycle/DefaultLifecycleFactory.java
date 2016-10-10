package com.unnsvc.rhena.lifecycle;

import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModule;

public class DefaultLifecycleFactory implements ILifecycleFactory {

	@Override
	public IResourcesProcessor newResourceProcessor(RhenaModule model, RhenaExecutionType type) {

		return new DefaultResourcesProcessor(model, type);
	}

}
