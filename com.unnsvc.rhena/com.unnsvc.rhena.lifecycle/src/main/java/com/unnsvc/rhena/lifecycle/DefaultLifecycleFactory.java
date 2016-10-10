package com.unnsvc.rhena.lifecycle;

import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModel;

public class DefaultLifecycleFactory implements ILifecycleFactory {

	@Override
	public IResourcesProcessor newResourceProcessor(RhenaModel model, RhenaExecutionType type) {

		return new DefaultResourcesProcessor(model, type);
	}

}
