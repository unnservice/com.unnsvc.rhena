package com.unnsvc.rhena.lifecycle;

import com.unnsvc.rhena.common.model.CompositeScope;
import com.unnsvc.rhena.common.model.RhenaModule;

public interface ILifecycleFactory {

	public ILifecycle newLifecycle(RhenaModule model, CompositeScope scope);

}
